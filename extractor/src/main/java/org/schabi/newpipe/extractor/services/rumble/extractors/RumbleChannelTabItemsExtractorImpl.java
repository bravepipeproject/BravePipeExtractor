package org.schabi.newpipe.extractor.services.rumble.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;

import java.util.List;

public class RumbleChannelTabItemsExtractorImpl extends RumbleBrowseLiveItemExtractorImpl {

    @Override
    public List<StreamInfoItemExtractor> extractStreamItems(final Document doc)
            throws ParsingException {
        final Element script = doc.selectFirst("rum-videos-grid script[type=application/json]");
        if (script == null) {
            return List.of();
        }

        final JsonArray items;
        try {
            items = JsonParser.object().from(script.data()).getArray("items");
        } catch (final JsonParserException e) {
            throw new ParsingException("Could not parse the channel listing JSON", e);
        }
        return RumbleJsonVideoItemHelper.toInfoItems(items);
    }
}
