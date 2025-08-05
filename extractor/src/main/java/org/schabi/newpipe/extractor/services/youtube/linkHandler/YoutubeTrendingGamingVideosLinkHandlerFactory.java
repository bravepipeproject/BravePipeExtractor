package org.schabi.newpipe.extractor.services.youtube.linkHandler;

import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.isInvidiousURL;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.isYoutubeURL;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.search.filter.FilterItem;
import org.schabi.newpipe.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class YoutubeTrendingGamingVideosLinkHandlerFactory extends ListLinkHandlerFactory {

    public static final String KIOSK_ID = "trending_gaming";

    public static final YoutubeTrendingGamingVideosLinkHandlerFactory INSTANCE =
            new YoutubeTrendingGamingVideosLinkHandlerFactory();

    private YoutubeTrendingGamingVideosLinkHandlerFactory() {
    }

    @Override
    public String getUrl(final String id,
                         @Nonnull final List<FilterItem> contentFilter,
                         @Nullable final List<FilterItem> sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return "https://www.youtube.com/gaming/trending";
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return KIOSK_ID;
    }

    @Override
    public boolean onAcceptUrl(final String url) {
        final URL urlObj;
        try {
            urlObj = Utils.stringToURL(url);
        } catch (final MalformedURLException e) {
            return false;
        }

        return Utils.isHTTP(urlObj) && (isYoutubeURL(urlObj) || isInvidiousURL(urlObj))
                && "/gaming/trending".equals(urlObj.getPath());
    }
}
