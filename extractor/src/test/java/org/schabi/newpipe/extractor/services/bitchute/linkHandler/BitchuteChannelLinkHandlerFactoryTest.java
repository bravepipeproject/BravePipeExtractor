package org.schabi.newpipe.extractor.services.bitchute.linkHandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link BitchuteChannelLinkHandlerFactory}
 */
public class BitchuteChannelLinkHandlerFactoryTest {

    private static BitchuteChannelLinkHandlerFactory linkHandler;


    @BeforeAll
    public static void setUp() {
        linkHandler = BitchuteChannelLinkHandlerFactory.getInstance();
        NewPipe.init(DownloaderTestImpl.getInstance());
    }

    @Test
    public void acceptUrlTest() throws ParsingException {
        final String[] acceptedChannelUrls = {
                "https://www.bitchute.com/channel/u3QMwGD7bSW6",
                "https://www.bitchute.com/channel/missoncommander/"
        };

        for (final String acceptedChannelUrl : acceptedChannelUrls) {
            assertTrue(linkHandler.acceptUrl(acceptedChannelUrl));
        }

        final String[] baseVideoUrls = {
                "https://www.bitchute.com/video/",
                "https://www.bitchute.com/embed/"
        };
        final String[] videoIds = {"m4iEhq4L1qU", "8gwdyYJ8BUk"};

        // do not accept URLs which are not channels
        for (final String baseUrl : baseVideoUrls) {
            for (final String videoId : videoIds) {
                final String thisIsNoChannelUrl = baseUrl + videoId;
                assertFalse(linkHandler.acceptUrl(thisIsNoChannelUrl));
            }
        }
    }

    @Test
    public void getId() throws ParsingException {
        final String[] acceptedChannelUrls = {
                "https://www.bitchute.com/channel/u3QMwGD7bSW6",
                "https://www.bitchute.com/channel/missoncommander"
        };
        final String[] expectedChannelIds = {
                "u3QMwGD7bSW6",
                "missoncommander"
        };

        for (int i = 0; i < acceptedChannelUrls.length; i++) {
            final String acceptedChannelUrl = acceptedChannelUrls[i];
            final String expectedChannelId = expectedChannelIds[i];

            assertEquals(expectedChannelId,
                    linkHandler.fromUrl(acceptedChannelUrl).getId());
        }
    }

    @Test
    public void getUrl() throws ParsingException {
        final String[] expectedChannelUrls = {
                "https://www.bitchute.com/channel/u3QMwGD7bSW6",
                "https://www.bitchute.com/channel/missoncommander"
        };
        final String[] acceptedChannelIds = {
                "u3QMwGD7bSW6",
                "missoncommander"
        };

        for (int i = 0; i < expectedChannelUrls.length; i++) {
            final String expectedChannelUrl = expectedChannelUrls[i];
            final String acceptedChannelId = acceptedChannelIds[i];
            final String badChannelId = "channel/" + acceptedChannelId;

            final ParsingException what = assertThrows(ParsingException.class,
                    () -> linkHandler.fromId(badChannelId).getUrl());
            assertTrue(what instanceof ParsingException);

            assertEquals(expectedChannelUrl,
                    linkHandler.fromId(acceptedChannelId).getUrl());
        }
    }
}
