package org.schabi.newpipe.extractor.services.bitchute.linkHandler;

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

public final class BitchuteChannelTabLinkHandlerFactory extends ListLinkHandlerFactory {

    static final Map<FilterItem, String> TAB2URLSUFFIX = new HashMap<>() {{
        put(ChannelTabs.VIDEOS, "");
    }};

    private static final BitchuteChannelTabLinkHandlerFactory INSTANCE
            = new BitchuteChannelTabLinkHandlerFactory();

    private BitchuteChannelTabLinkHandlerFactory() {
    }

    public static BitchuteChannelTabLinkHandlerFactory getInstance() {
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
        return BitchuteChannelLinkHandlerFactory.getInstance().getId(url);
    }

    @Override
    public String getUrl(final String id,
                         @Nonnull final List<FilterItem> contentFilter,
                         @Nullable final List<FilterItem> sortFilter) throws ParsingException {
        return BitchuteChannelLinkHandlerFactory.getInstance().getUrl(id)
                + getUrlSuffix(contentFilter.get(0));
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return BitchuteChannelLinkHandlerFactory.getInstance().onAcceptUrl(url);
    }
}
