package org.schabi.newpipe.extractor.services.peertube.linkHandler;


import java.net.URL;

public final class BravePeertubeChannelLinkHandlerFactoryHelper {

    /**
     * Peertube channel check does not check for domain names.
     * <p>
     * It also should not check as everyone can host their own instance.
     * But as rumble.com has similar parts like /c/ in its channel urls
     * we have a problem.
     * -> the workaround is to check if the host is rumble.com and for
     *    completeness (even is maybe not necessary) we do the same for
     *    bitchute.com
     * -> if above hosts are found Peertube should not declare them
     *    their own urls.
     *
     * @param url the url man!
     * @return false if not a Peertube url.
     */
    public boolean onAcceptUrl(final URL url) {
        if (url.getHost().equals("rumble.com")
                || url.getHost().equals("bitchute.com")) {
            return false;
        }
        return true;
    }
}
