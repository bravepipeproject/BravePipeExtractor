package org.schabi.newpipe.downloader;

import org.schabi.newpipe.extractor.downloader.BraveCookieManager;

import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

final class BraveDownloaderTestImplUtils {

    private BraveDownloaderTestImplUtils() {
    }

    /**
     * Rumble needs to handle cookies to correctly redirect. It was
     *
     * Reported in
     * <a href="https://github.com/bravenewpipe/NewPipeExtractor/issues/123">issue#123</a>
     * even though it seems it was only temporary Rumble glitch this functionality is added here.
     *
     * Note: this code is duplicated from NewPipe#BraveDownloaderImplUtils class
     *
     * @param theBuilder the builder
     */
    public static void addCookieManager(final OkHttpClient.Builder theBuilder) {
        final BraveCookieManager cookieManager = new BraveCookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        theBuilder.cookieJar(new JavaNetCookieJar(cookieManager));
    }
}
