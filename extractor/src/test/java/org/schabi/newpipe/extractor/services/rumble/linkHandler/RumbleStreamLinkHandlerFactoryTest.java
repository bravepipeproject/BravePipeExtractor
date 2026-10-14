package org.schabi.newpipe.extractor.services.rumble.linkHandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link RumbleStreamLinkHandlerFactory}
 */
@SuppressWarnings({"checkstyle:LineLength", "checkstyle:InvalidJavadocPosition"})
public class RumbleStreamLinkHandlerFactoryTest {
    private static RumbleStreamLinkHandlerFactory linkHandler;

    @BeforeAll
    public static void setUp() {
        linkHandler = RumbleStreamLinkHandlerFactory.getInstance();
        NewPipe.init(DownloaderTestImpl.getInstance());
    }

    @Test
    public void getId() throws Exception {
        final String correctIdExpectSuccess = "vdofb67";
        final String tooShortIdExpectError = "vdof";
        final String noIdExpectError = "";


        final String[] baseUrls = {"https://rumble.com/"};

        /** {@value correctIdExpectSuccess} */
        for (final String baseUrl : baseUrls) {
            final String testUrl = baseUrl + correctIdExpectSuccess;
            assertEquals(correctIdExpectSuccess, linkHandler.fromUrl(testUrl).getId());

        }

        /** {@value tooShortIdExpectError} */
        for (final String baseUrl : baseUrls) {
            final String testUrl = baseUrl + tooShortIdExpectError;

            final ParsingException what = assertThrows(ParsingException.class,
                    () -> linkHandler.fromUrl(testUrl).getId());
            assertTrue(what instanceof ParsingException);
        }

        /** {@value noIdExpectError} */
        for (final String baseUrl : baseUrls) {
            final String testUrl = baseUrl + noIdExpectError;

            final ParsingException what = assertThrows(ParsingException.class,
                    () -> linkHandler.fromUrl(testUrl).getId());
            assertTrue(what instanceof ParsingException);
        }

        final String[] invalidVideoUrls = {
                "https://pumble.com",
                "https://sumble.com/vdofb7",
                "https://sumble.com/vd_ofb7",
                "https://rumble.com",
                "https://rumble.com/",
                "https://rumble.com/category/v23",
                "https://rumble.com/category/v23/",
                "https://rumble.com/user/Vlemx",
                "https://rumble.com/c/Vlemx",
                "https://rumble.com/user/vmpradio",
        };

        for (final String invalidVideoUrl : invalidVideoUrls) {
            assertThrows(ParsingException.class, () -> linkHandler.getId(invalidVideoUrl),
                    "This URL is invalid: " + invalidVideoUrl);
        }
    }

    @Test
    public void getUrl() throws Exception {
        final String inputVideoUrl = "https://rumble.com/vdofb7";
        //final String inputEmbedUrl = "https://www.rumble.com/embed/8gwdyYJ8BUk/";
        final String inputId = "vdofb7";

        final String expectedUrl = "https://rumble.com/vdofb7";

        assertEquals(expectedUrl,
                linkHandler.fromId(inputId).getUrl());
        assertEquals(expectedUrl,
                linkHandler.fromUrl(inputVideoUrl).getUrl());
        //assertEquals(expectedUrl,
        //        linkHandler.fromUrl(inputEmbedUrl).getUrl());
    }

    @Test
    public void testAcceptUrl() throws ParsingException {
        final String validShortVideoUrl = "https://rumble.com/vdofb7";
        final String validLongVideoUrl = "https://rumble.com/vg1hkl-youtube-ceo-wins-major-award-and-you-wont-believe-for-what.html";

        assertTrue(linkHandler.acceptUrl(validShortVideoUrl));
        assertTrue(linkHandler.acceptUrl(validLongVideoUrl));
    }
}
