package org.schabi.newpipe.extractor.downloader;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Manage the hosts we want to set cookies here.
 * <p>
 * This is done globally as setting it in the service extractor implementation in
 * NewPipeExtractor it is not possible as in cases of redirection and cookie usage
 * we have not yet fetched anything and might need to set cookies
 * to proceed.. This was the case with rumble reported on 20240302 in
 * <a href="https://github.com/bravenewpipe/NewPipeExtractor/issues/123">issue#123</a>
 * even though it seems it was only temporary Rumble glitch this functionality is added here.
 * <p>
 * The class has to be used globally within OkHttp. In our cases from within testing
 * (DownloaderTestImpl) here and in DownloaderImpl in NewPipe
 * <p>
 * TODO maybe add also bitchute it may help avoid some exceptions
 */
public class BraveCookieManager extends CookieManager {

    final String[] hosts2AcceptCookiesFor = {"rumble."};

    @Override
    public void put(
            final URI uri,
            final Map<String, List<String>> responseHeaders) throws IOException {
        final String host = uri.getHost();
        for (final String hostThatAcceptsCookie : hosts2AcceptCookiesFor) {
            if (host.contains(hostThatAcceptsCookie)) {
                super.put(uri, responseHeaders);
                return;
            }
        }
    }
}
