package org.schabi.newpipe.extractor.services.rumble.linkHandler;

import org.schabi.newpipe.extractor.brave.AttachException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandlerFactory;
import org.schabi.newpipe.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public final class RumbleStreamLinkHandlerFactory extends LinkHandlerFactory {

    private static final RumbleStreamLinkHandlerFactory INSTANCE =
            new RumbleStreamLinkHandlerFactory();

    static final String BASE_URL = "https://rumble.com";
    private String patternMatchId = "^v[a-zA-Z0-9]{4,}-?";

    private RumbleStreamLinkHandlerFactory() {
    }

    public static RumbleStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    private String assertsID(final String id) throws ParsingException {
        if (id == null || !id.matches(patternMatchId)) {
            throw new ParsingException("Given string is not a Rumble Video ID");
        }
        return id;
    }

    @Override
    public String getUrl(final String id) throws ParsingException {
        return BASE_URL + "/" + assertsID(id);
    }

    @Override
    public String getId(final String urlString) throws ParsingException {
        final URL url;
        try {
            url = Utils.stringToURL(urlString);
            if (!url.getAuthority().equals(Utils.stringToURL(BASE_URL).getAuthority())
                    || !url.getProtocol().equals(Utils.stringToURL(BASE_URL).getProtocol())) {
                throw new MalformedURLException();
            }
        } catch (final MalformedURLException e) {
            final AttachException exception =
                    new AttachException("The given URL is not valid: " + urlString);
            exception.addExceptionData(e.getMessage());
            throw exception;
        }

        String videoId = null;
        final String[] pathParts = url.getPath().split("/");
        // url.getPath() returns a path starting with '/'
        // -> therefore we expect 2 elements
        if (pathParts.length < 2) {
            throw new ParsingException("Error getting ID: " + url.getPath());
        }

        try {
            // 1. the pathParts[1] has to be the videoId
            // 2. split after first '-' as this is the separator between id and remaining url
            final String[] splitPath = pathParts[1].split("-", 0);
            videoId = splitPath[0];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ParsingException("Error getting ID");
        }

        return assertsID(videoId);
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        try {
            getId(url);
            return true;
        } catch (final ParsingException e) {
            return false;
        }
    }
}
