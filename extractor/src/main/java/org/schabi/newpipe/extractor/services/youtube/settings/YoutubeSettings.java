package org.schabi.newpipe.extractor.services.youtube.settings;

import org.schabi.newpipe.extractor.settings.ServiceSettings;

public final class YoutubeSettings extends ServiceSettings {
    public static final int HIDE_MEMBERS_ONLY_STREAMS = 1;

    private static final YoutubeSettings INSTANCE =
            new YoutubeSettings();

    private YoutubeSettings() {
    }

    public static YoutubeSettings getInstance() {
        return INSTANCE;
    }
}
