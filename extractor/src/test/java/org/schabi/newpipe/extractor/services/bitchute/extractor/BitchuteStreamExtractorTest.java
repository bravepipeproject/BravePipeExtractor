package org.schabi.newpipe.extractor.services.bitchute.extractor;

import com.grack.nanojson.JsonObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderFactory;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.services.DefaultStreamExtractorTest;
import org.schabi.newpipe.extractor.services.bitchute.BitchuteParserHelper;
import org.schabi.newpipe.extractor.services.bitchute.linkHandler.BitchuteCommentsLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.bitchute.misc.BitchuteHelpers;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.schabi.newpipe.downloader.DownloaderFactory.RESOURCE_PATH;
import static org.schabi.newpipe.extractor.ServiceList.Bitchute;

public class BitchuteStreamExtractorTest extends DefaultStreamExtractorTest {

    private static BitchuteStreamExtractor extractor;

    private static String url = "https://www.bitchute.com/video/m4iEhq4L1qU/";
    private static String expectedUrl = "https://www.bitchute.com/video/m4iEhq4L1qU";
    private static String expectedName = "BATTLE BUS LIVE | THE WAY FORWARD FOR LONDON - TRAILER";
    private static String expectedId = "m4iEhq4L1qU";
    private static String expectedDesc = "#BrianForMayor";
    private static String expectedCategory = "Entertainment";
    private static int expectedAgeLimit = 16;
    private static long expectedViewCountAtLeast = 230;
    private static String expectedUploaderName = "London Real";
    private static String expectedUploadDate = "2021-04-06 19:26:00.000";
    private static String expectedTextualUploadDate =
            "First published at 19:26 UTC on April 6th, 2021.";
    private static StreamExtractor.Privacy expectedPrivacy = StreamExtractor.Privacy.OTHER;
    private static String expectedUploaderUrl = "https://www.bitchute.com/channel/londonrealtv/";
    private static String expectedSupportInfo = "https://www.bitchute.com/help-us-grow/";
    private static boolean expectedHasAudioStreams = false;
    private static boolean expectedHasVideoStreams = true;
    private static String expectedArtistProfilePictureInfix = ".bitchute.com/live/channel_images/";

    protected static final String MOCK_PATH =
            RESOURCE_PATH + "/services/bitchute/extractor/stream/";

    @BeforeAll
    public static void setUp() throws ExtractionException, IOException {
        System.setProperty("downloader", "MOCK");
        //System.setProperty("downloader", "RECORDING");

        NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH
                + "/streamExtractor"));

        extractor = (BitchuteStreamExtractor) Bitchute
                .getStreamExtractor(url);
        extractor.fetchPage();
    }

    @Override
    public StreamExtractor extractor() {
        return extractor;
    }

    @Override
    public StreamingService expectedService() {
        return Bitchute;
    }

    @Override
    public String expectedName() {
        return expectedName;
    }

    @Override
    public String expectedId() {
        return expectedId;
    }

    @Override
    public String expectedUrlContains() {
        return expectedUrl;
    }

    @Override
    public String expectedOriginalUrlContains() {
        return expectedUrl;
    }

    @Override
    public StreamType expectedStreamType() {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public String expectedUploaderName() {
        return expectedUploaderName;
    }

    @Override
    public String expectedUploaderUrl() {
        return expectedUploaderUrl;
    }

    @Override
    public List<String> expectedDescriptionContains() {
        return Collections.singletonList(expectedDesc);
    }

    @Override
    public long expectedLength() {
        return 107;
    }

    @Test
    @Override
    public void testLength() throws Exception {
        // 0. start with a cleared cache
        BitchuteHelpers.VideoDurationCache.resetCache();
        // 1. test if the empty cache throws
        assertThrows(NoSuchElementException.class,
                () -> BitchuteHelpers
                        .VideoDurationCache.getDurationForVideoId(extractor().getId()));
        // 2. execute normal test
        super.testLength();
        // 3. test if the cache has expected duration
        assertEquals(BitchuteHelpers
                        .VideoDurationCache.getDurationForVideoId(extractor().getId()),
                expectedLength());
    }

    @Override
    public long expectedViewCountAtLeast() {
        return expectedViewCountAtLeast;
    }

    @Override
    public String expectedUploadDate() {
        return expectedUploadDate;
    }

    @Override
    public String expectedTextualUploadDate() {
        return expectedTextualUploadDate;
    }

    @Override
    public long expectedLikeCountAtLeast() {
        return Long.MIN_VALUE;
    }

    @Override
    public long expectedDislikeCountAtLeast() {
        return Long.MIN_VALUE;
    }

    @Override
    public boolean expectedHasVideoStreams() {
        return expectedHasVideoStreams;
    }

    @Override
    public boolean expectedHasRelatedItems() {
        return true;
    }

    @Override
    public boolean expectedHasSubtitles() {
        return false;
    }

    @Override
    public boolean expectedHasFrames() {
        return false;
    }

    @Override
    public String expectedCategory() {
        return expectedCategory;
    }

    @Test
    public void testArtistProfilePicture() throws Exception {
        final String uploaderAvatarUrl = extractor().getUploaderAvatars().get(0).getUrl();
        assertTrue(uploaderAvatarUrl.contains(expectedArtistProfilePictureInfix)
                && uploaderAvatarUrl.endsWith(".jpg"));
    }

    @Override
    public int expectedAgeLimit() {
        return expectedAgeLimit;
    }

    @Override
    public StreamExtractor.Privacy expectedPrivacy() {
        return expectedPrivacy;
    }

    @Override
    public String expectedSupportInfo() {
        return expectedSupportInfo;
    }

    @Override
    public boolean expectedHasAudioStreams() {
        return expectedHasAudioStreams;
    }

    @Test
    @Disabled
    // this is only some initial testing code that is no unit test at all.
    public void testComments() throws ExtractionException, IOException {
        final String localUrl = "https://www.bitchute.com/video/AVcFaBG8Fj3O/";
        final var comments = BitchuteParserHelper.getComments("AVcFaBG8Fj3O", localUrl, 0);

        for (final Object comment : comments) {
            final var ce = new BitchuteCommentsInfoItemExtractor((JsonObject) comment, url);
            System.out.println("id  " + ce.getCommentId());
            System.out.println("txt " + ce.getCommentText());
            System.out.println("sp  " + ce.getStreamPosition());
            System.out.println("nam " + ce.getName());

        }

        final var lf = BitchuteCommentsLinkHandlerFactory.getInstance();
        final var lh = lf.fromUrl(localUrl);

        final var service = Bitchute;
        service.getCommentsExtractor(lh);

        final var snack = new BitchuteCommentsExtractor(NewPipe.getService("BitChute"), lh);
        snack.getInitialPage();

        System.out.println("done");
    }
}
