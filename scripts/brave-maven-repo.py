#!/usr/bin/env python3
# License: GPL_v3
# Author: evermind
# Version: 1.1.0
#
# Changelog:
# - v1.0.0 (20250808):
#   * Initial version
# - v1.1.0 (20250904):
#   * Added options --clean-m2 --overwrite
#   * Added option --custom-tag to override git tag detection
# - v1.1.1 (20260123):
#   * drop {build,settings}.gradle and use {build,settings}.build.kts

"""
This script automates the version update and local Maven publishing process
for a multi-module Gradle project. Intented to be used with BravePipeExtractor
but should be useable if adjusted with other projects too.

It is used to easily push it later to a custom repo as jitpack.io is sometimes
not reliable.

Features:
- Retrieves the current Git tag and uses it as the version in `build.gradle.kts`.
- Updates the root project name in `settings.gradle`.
- Temporarily overrides the default Maven local repository (`~/.m2/repository`)
  with a custom directory to isolate published artifacts.
- Automatically restores the original Maven repository on exit or interruption,
  using a safe and race-condition-free cleanup mechanism.
- Starts and stops the Gradle daemon only for the duration of the script
  to avoid interfering with other Gradle processes.
- Publishes a predefined list of subprojects using `publishToMavenLocal`.
- adjust Constants as needed.

Intended for internal use to prepare and publish BravePipe artifacts locally
without polluting the global Maven repository.
"""

import subprocess
import os
import re
import shutil
import time
import signal
import sys
import tarfile
import tempfile
import argparse
from pathlib import Path
from threading import Lock

# Constants
BUILD_FILE = Path("build.gradle.kts")
EXT_BUILD_FILE = Path("extractor/build.gradle.kts")
SETTINGS_FILE = Path("settings.gradle.kts")
GRADLE_BIN = "./gradlew"
MAVEN_REPO_TEMP = "/tmp/local-maven-publish-repo"
M2_REPO = Path.home() / ".m2" / "repository"
M2_BACKUP = Path.home() / f".m2/repository.bak.{int(time.time())}"
PROJECTS = ["extractor"] #, "timeago-generator", "timeago-parser"]
DAEMON_PID_FILE = ".gradle-daemon-pid"
PROJECT_GROUP = "com.github.bravepipeproject"
ROOT_PROJECT_NAME = "BravePipeExtractor"

ARGS = None  # global parsed args

def get_git_tag():
    if ARGS and ARGS.custom_tag:
        print(f"🏷  Using custom tag from CLI: {ARGS.custom_tag}")
        return ARGS.custom_tag
    try:
        tag = subprocess.check_output(["git", "describe", "--tags", "--abbrev=0"], text=True).strip()
        return tag
    except subprocess.CalledProcessError:
        raise RuntimeError("❌ Could not determine current Git tag (and no --custom-tag provided).")

def update_build_gradle_kts(tag):
    content = BUILD_FILE.read_text()
    content = re.sub(r"version\s*=\s*['\"].*?['\"]", f"version = \"{tag}\"", content)
    BUILD_FILE.write_text(content)
    print(f"✔ Updated {BUILD_FILE}")

def update_settings_gradle_kts():
    content = SETTINGS_FILE.read_text()
    content = re.sub(
        r"(rootProject\.name\s*=\s*)['\"].*?['\"]",
        fr'\1"{ROOT_PROJECT_NAME}"',
        content
    )
    SETTINGS_FILE.write_text(content)
    print(f"✔ Updated {SETTINGS_FILE}")

def update_extractor_build_gradle_kts():
    content = EXT_BUILD_FILE.read_text()
    content = re.sub(r"groupId\s*=\s*['\"].*?['\"]", f"groupId = \"{PROJECT_GROUP}\"", content)
    EXT_BUILD_FILE.write_text(content)
    print(f"✔ Updated {EXT_BUILD_FILE}")

def start_gradle_daemon():
    print("⚙️  Starting Gradle daemon...")
    # Start Gradle with daemon mode to spawn the background daemon process
    subprocess.run([GRADLE_BIN, "--daemon", "help"], check=True)

    time.sleep(2)  # Allow the daemon to start properly

    # Get list of running Gradle daemons
    output = subprocess.check_output([GRADLE_BIN, "--status"], text=True)

    # Extract PID(s) of active daemons
    daemons = re.findall(r'^\s*PID\s+(\d+)', output, re.MULTILINE)
    if daemons:
        daemon_pid = daemons[-1]  # Use the last daemon started
        Path(DAEMON_PID_FILE).write_text(daemon_pid)
        print(f"🟢 Gradle daemon started (PID: {daemon_pid})")
    else:
        print("⚠️ Could not determine Gradle daemon PID.")

def stop_own_gradle_daemon():
    if Path(DAEMON_PID_FILE).exists():
        daemon_pid = Path(DAEMON_PID_FILE).read_text().strip()
        print(f"🛑 Stopping Gradle daemon with PID: {daemon_pid}")
        subprocess.run([GRADLE_BIN, "--stop"])
        Path(DAEMON_PID_FILE).unlink(missing_ok=True)

def backup_m2_repo(clean=False):
    if M2_REPO.exists():
        if clean:
            print(f"⚠️ Deleting existing {M2_REPO}")
            shutil.rmtree(M2_REPO)
        else:
            print(f"📁 Moving existing {M2_REPO} to {M2_BACKUP}")
            shutil.move(M2_REPO, M2_BACKUP)
    M2_REPO.mkdir(parents=True, exist_ok=True)

def restore_m2_repo():
    if not Path(MAVEN_REPO_TEMP).exists():
        print(f"📦 Moving published artifacts to {MAVEN_REPO_TEMP}")
        shutil.move(M2_REPO, MAVEN_REPO_TEMP)
    if M2_BACKUP.exists():
        if not M2_REPO.exists():
            print(f"🔄 Restoring original Maven repository from {M2_BACKUP}")
            shutil.move(M2_BACKUP, M2_REPO)
        else:
            print(f"❌ Error: {M2_REPO} already exists. Cannot restore from {M2_BACKUP}, please look manually")
    else:
        print(f"ℹ️ No original Maven repo backup found – skipping restore")

def publish_projects():
    for project in PROJECTS:
        print(f"🚀 Publishing {project} ...")
        prefix_project = f":{project}" if project else ""
        result = subprocess.run([GRADLE_BIN, f"{prefix_project}:publishToMavenLocal"])
        if result.returncode != 0:
            raise RuntimeError(f"❌ Failed to publish {project}")

def create_repo_tarball(repo_dir, target_dir):
    """
    Create a tar.gz archive of the given Maven repository directory.
    The tarball is created in /tmp and then moved to the target directory.

    :param repo_dir: Path to the Maven repository directory to archive.
    :param target_dir: Directory where the final tarball should be placed.
    """
    if not os.path.isdir(repo_dir):
        raise ValueError(f"Directory not found: {repo_dir}")
    if not os.path.isdir(target_dir):
        raise ValueError(f"Target directory not found: {target_dir}")

    tmp_fd, tmp_tar_path = tempfile.mkstemp(suffix=".tar.gz", prefix="repo-snapshot-", dir="/tmp")
    os.close(tmp_fd)  # We only need the path, not the open file descriptor

    try:
        with tarfile.open(tmp_tar_path, "w:gz") as tar:
            for item in Path(repo_dir).iterdir():
                tar.add(item, arcname=item.name)

        print(f"📦 Created temporary tarball: {tmp_tar_path}")

        final_path = Path(target_dir) / "repo-snapshot.tar.gz"
        shutil.move(tmp_tar_path, final_path)
        print(f"✅ Moved tarball to: {final_path}")
    except Exception as e:
        # Clean up temp file on error
        if os.path.exists(tmp_tar_path):
            os.remove(tmp_tar_path)
        raise RuntimeError(f"Failed to create tarball: {e}")

cleanup_lock = Lock()
cleanup_done = False
# Trap handler for signals
def cleanup_and_exit(signum=None, frame=None):
    global cleanup_done
    with cleanup_lock:
        if cleanup_done:
            return  # Prevent double execution
        cleanup_done = True

        print(f"\n⚠️ Caught signal {signum}. Cleaning up safely...")

        # Block further signals during cleanup
        signal.signal(signal.SIGINT, signal.SIG_IGN)
        signal.signal(signal.SIGTERM, signal.SIG_IGN)

        try:
            restore_m2_repo()
            if not (ARGS and ARGS.no_tarball):
                create_repo_tarball(MAVEN_REPO_TEMP, MAVEN_REPO_TEMP)
        except Exception as e:
            print(f"❗ Failed to restore Maven repo: {e}")

        try:
            stop_own_gradle_daemon()
        except Exception as e:
            print(f"❗ Failed to stop Gradle daemon: {e}")

        print("🧹 Cleanup complete. Exiting.")
        sys.exit(1 if signum else 0)

# Register signal handlers
signal.signal(signal.SIGINT, cleanup_and_exit)   # Ctrl+C
signal.signal(signal.SIGTERM, cleanup_and_exit)  # kill <pid>

def main():
    global ARGS
    parser = argparse.ArgumentParser(description="Brave Maven Repo Builder")
    parser.add_argument("--write-to-current-m2", action="store_true",
                        help="Write artifacts directly to current ~/.m2/repository (no backup/restore)")
    parser.add_argument("--clean-m2", action="store_true",
                        help="Delete existing ~/.m2/repository before publishing")
    parser.add_argument("--no-tarball", action="store_true",
                        help="Do not create a tarball of the published Maven repository")
    parser.add_argument("--custom-tag", type=str,
                        help="Use a custom tag instead of detecting from git")
    ARGS = parser.parse_args()

    if not ARGS.write_to_current_m2 and Path(MAVEN_REPO_TEMP).exists():
        if ARGS.clean_m2:
            print(f"⚠️ Removing old {MAVEN_REPO_TEMP}")
            shutil.rmtree(MAVEN_REPO_TEMP)
        else:
            print(f"📦 {MAVEN_REPO_TEMP} already exists – remove first or use --clean-m2")
            return 1

    try:
        tag = get_git_tag()
        print(f"🏷  Using Git tag: {tag}")
        update_build_gradle_kts(tag)
        update_settings_gradle_kts()
        update_extractor_build_gradle_kts()
        if ARGS.write_to_current_m2:
            print("📝 Writing directly into current ~/.m2/repository")
        else:
            backup_m2_repo(clean=ARGS.clean_m2)
        start_gradle_daemon()
        publish_projects()
    finally:
        if not ARGS.write_to_current_m2:
            cleanup_and_exit()
        else:
            try:
                stop_own_gradle_daemon()
            except Exception as e:
                print(f"❗ Failed to stop Gradle daemon: {e}")

if __name__ == "__main__":
    sys.exit(main())
