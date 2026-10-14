package org.schabi.newpipe.extractor.services.rumble.linkHandler;

import org.schabi.newpipe.extractor.channel.tabs.ChannelTabs;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.UnsupportedTabException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.search.filter.FilterItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RumbleChannelTabLinkHandlerFactory extends ListLinkHandlerFactory {

    static final Map<FilterItem, String> TAB2URLSUFFIX = new HashMap<>() {{
        put(ChannelTabs.VIDEOS, "/videos");
        put(ChannelTabs.LIVESTREAMS, "/livestreams");
    }};
    private static final RumbleChannelTabLinkHandlerFactory INSTANCE
            = new RumbleChannelTabLinkHandlerFactory();

    private RumbleChannelTabLinkHandlerFactory() {
    }

    public static RumbleChannelTabLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Nonnull
    public static String getUrlSuffix(final FilterItem tab) throws UnsupportedOperationException {
        if (TAB2URLSUFFIX.containsKey(tab)) {
            return TAB2URLSUFFIX.get(tab);
        }

        throw new UnsupportedTabException(tab);
    }

    @Nonnull
    public static Map<FilterItem, String> getTab2UrlSuffixes() {
        return TAB2URLSUFFIX;
    }

    @Override
    public String getId(final String url) throws ParsingException {
        return RumbleChannelLinkHandlerFactory.getInstance().getId(url);
    }

    @Override
    public String getUrl(final String id,
                         @Nonnull final List<FilterItem> contentFilter,
                         @Nullable final List<FilterItem> sortFilter) throws ParsingException {
        return RumbleChannelLinkHandlerFactory.getInstance().getUrl(id)
                + getUrlSuffix(contentFilter.get(0));
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return RumbleChannelLinkHandlerFactory.getInstance().onAcceptUrl(url);
    }
}
