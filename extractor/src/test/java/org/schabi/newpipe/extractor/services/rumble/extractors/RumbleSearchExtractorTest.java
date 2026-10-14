package org.schabi.newpipe.extractor.services.rumble.extractors;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.schabi.newpipe.downloader.DownloaderFactory.RESOURCE_PATH;
import static org.schabi.newpipe.extractor.ServiceList.Rumble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderFactory;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.search.filter.FilterItem;
import org.schabi.newpipe.extractor.services.DefaultSearchExtractorTest;

import org.schabi.newpipe.extractor.channel.ChannelInfoItem;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.services.rumble.search.filter.RumbleFilters;
import org.schabi.newpipe.extractor.utils.Utils;

import javax.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertEmptyErrors;
import static org.schabi.newpipe.extractor.services.DefaultTests.assertNoDuplicatedItems;

@SuppressWarnings({"checkstyle:LineLength", "checkstyle:InvalidJavadocPosition", "checkstyle:LeftCurly"})
public class RumbleSearchExtractorTest {

    private static final String MOCK_PATH =
            RESOURCE_PATH + "/services/rumble/extractor/search/";

    public static class MultiplePagesResults extends AbstractSearchBaseTest {

        @BeforeAll
        public static void setUp() throws Exception {
            query = "paul";
            expectedSearchBaseUrl = "rumble.com/search/video?q=";
            expectedSearchOriginalBaseUrl = "rumble.com/search/video?q=";

            System.setProperty("downloader", "MOCK");
//            System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH
                    + "/MultiplePagesResults"));
            final FilterItem videoFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_VIDEOS);
            extractor = (RumbleSearchExtractor)
                    Rumble.getSearchExtractor(query, singletonList(videoFilterItem), null);
            extractor.fetchPage();
        }

        // override tests
        @Override public InfoItem.InfoType expectedInfoItemType() {
            return InfoItem.InfoType.STREAM;
        }

        @Override
        public void testMoreRelatedItems() throws Exception {
            super.testMoreRelatedItems();
        }

        /**
         *
         *  Test for {@link RumbleSearchVideoStreamInfoItemExtractor}
         */
        @Test
        public void rumbleSearchVideoStreamInfoItemExtractorTest()
                throws ExtractionException, IOException {

            /** more info see: {@link RumbleSharedTests#infoItemsResultsTest} */
            final String[] someExpectedResults = {
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Rand Paul', textualUploadDate='2023-01-08T17:45:00-04:00', viewCount=5990, duration=97, uploaderUrl='https://rumble.com/c/RandPaul', infoType=STREAM, serviceId=6, url='https://rumble.com/v242kwk-dr.-paul-breaks-ground-for-cumberland-family-medical-center-october-18-2022.html', name='Dr. Paul Breaks Ground For Cumberland Family Medical Center - October 18, 2022', thumbnailUrl='https://sp.rmbl.ws/s8/6/u/j/A/N/ujANh.oq1b.1.jpg', uploaderVerified='false'}",
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Rand Paul', textualUploadDate='2023-01-03T16:28:50-04:00', viewCount=5500, duration=145, uploaderUrl='https://rumble.com/c/RandPaul', infoType=STREAM, serviceId=6, url='https://rumble.com/v23r4ye-senator-rand-paul-officially-sworn-in-as-member-of-the-118th-congress.html', name='Senator Rand Paul Officially Sworn in as Member of the 118th Congress', thumbnailUrl='https://sp.rmbl.ws/s8/6/w/Z/x/L/wZxLh.oq1b.jpg', uploaderVerified='false'}",
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='The Ron Paul Liberty Report', textualUploadDate='2022-12-28T12:41:53-04:00', viewCount=5070, duration=1152, uploaderUrl='https://rumble.com/c/RonPaulLibertyReport', infoType=STREAM, serviceId=6, url='https://rumble.com/v22sow6-flashback-2017-julian-assange-speaks-out-at-ron-paul-institute-conference.html', name='Flashback 2017: Julian Assange Speaks Out At Ron Paul Institute Conference', thumbnailUrl='https://sp.rmbl.ws/s8/1/g/C/p/F/gCpFh.gq1b.2-small-Flashback-2017-Julian-Assan.jpg', uploaderVerified='false'}"
            };

            RumbleSharedTests.infoItemsResultsTest(extractor.getService(),
                    extractor.getInitialPage().getItems(),
                    extractor.getInitialPage().getErrors(),
                    someExpectedResults
            );
        }
    }

    public static class OnlyOnePageResults extends AbstractSearchBaseTest {

        @BeforeAll
        public static void setUp() throws Exception {
            query = "tigertest";
            expectedSearchBaseUrl = "rumble.com/search/video?q=";
            expectedSearchOriginalBaseUrl = "rumble.com/search/video?q=";

            System.setProperty("downloader", "MOCK");
//            System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "/OnlyOnePageResults"));
            final FilterItem videoFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_VIDEOS);
            final FilterItem channelsFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_CHANNELS);
            extractor = (RumbleSearchExtractor)
                Rumble.getSearchExtractor(query, singletonList(videoFilterItem), null);
            extractor.fetchPage();
        }

        // override tests
        @Test
        public void testMoreRelatedItems() throws Exception {
            final ListExtractor.InfoItemsPage<InfoItem> initialPage = extractor().getInitialPage();
            assertFalse(initialPage.hasNextPage(), "More items available when it shouldn't");
        }
    }

    public static class NoResultsAtAll extends AbstractSearchBaseTest {

        @BeforeAll
        public static void setUp() throws Exception {
            query = "p3423n dkje ";
            expectedSearchBaseUrl = "rumble.com/search/video?q=";
            expectedSearchOriginalBaseUrl = "rumble.com/search/video?q=";

            System.setProperty("downloader", "MOCK");
//            System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "/NoResultsAtAll"));
            final FilterItem videoFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_VIDEOS);
            extractor = (RumbleSearchExtractor)
                    Rumble.getSearchExtractor(query, singletonList(videoFilterItem), null);
            extractor.fetchPage();
        }

        // override tests
        @Test
        public void testRelatedItems() throws Exception {
            final ListExtractor.InfoItemsPage initialPage = extractor.getInitialPage();
            assertFalse(initialPage.hasNextPage(), "Does have more items, when it should not");
            final List items = initialPage.getItems();
            assertTrue(items.isEmpty(), "Page items list is empty");
            assertEmptyErrors("Page have errors", initialPage.getErrors());
        }
        @Test
        public void testMoreRelatedItems() throws Exception {
            final ListExtractor.InfoItemsPage<InfoItem> initialPage = extractor().getInitialPage();
            assertFalse(initialPage.hasNextPage(), "More items available when it shouldn't");
        }
    }

    public static class Channel extends AbstractSearchBaseTest {


        @BeforeAll
        public static void setUp() throws Exception {
            query = "mark";
            expectedSearchBaseUrl = "rumble.com/search/channel?q=";
            expectedSearchOriginalBaseUrl = "rumble.com/search/channel?q=";

            System.setProperty("downloader", "MOCK");
            // System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "/channel"));
            final FilterItem channelFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_CHANNELS);
            extractor =
                    Rumble.getSearchExtractor(query, singletonList(channelFilterItem), null);
            extractor.fetchPage();
        }

        // override tests
        @Override public InfoItem.InfoType expectedInfoItemType() {
            return InfoItem.InfoType.CHANNEL;
        }

        @Disabled("Test not relevant here")
        @Override
        public void testMoreRelatedItems() throws Exception { }
    }

    public static class PagingTest {
        @Test
        public void duplicatedItemsCheck() throws Exception {
            System.setProperty("downloader", "MOCK");
//            System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "/paging"));
            final FilterItem videoFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_VIDEOS);
            final SearchExtractor extractor =
                    Rumble.getSearchExtractor("facts", singletonList(videoFilterItem), null);
            extractor.fetchPage();

            final ListExtractor.InfoItemsPage<InfoItem> page1 = extractor.getInitialPage();
            final ListExtractor.InfoItemsPage<InfoItem> page2 = extractor.getPage(page1.getNextPage());

            assertNoDuplicatedItems(Rumble, page1, page2);
        }
    }

    public static class ChannelVerified extends AbstractSearchBaseTest {

        @BeforeAll
        public static void setUp() throws Exception {
            query = "Mark Dice";
            expectedSearchBaseUrl = "rumble.com/search/channel?q=";
            expectedSearchOriginalBaseUrl = "rumble.com/search/channel?q=";

            System.setProperty("downloader", "MOCK");
//            System.setProperty("downloader", "RECORDING");
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "verified"));

            final FilterItem channelFilterItem = DefaultSearchExtractorTest
                    .getFilterItem(Rumble, RumbleFilters.ID_CF_MAIN_CHANNELS);
            extractor = Rumble.getSearchExtractor(query, singletonList(channelFilterItem), null);
            extractor.fetchPage();
        }

        @Test
        public void testAtLeastOneVerified() throws IOException, ExtractionException {
            final List<InfoItem> items = extractor.getInitialPage().getItems();
            boolean verified = false;
            for (final InfoItem item : items) {
                if (((ChannelInfoItem) item).isVerified()) {
                    verified = true;
                    break;
                }
            }

            assertTrue(verified);
        }

        // override tests
        @Override public InfoItem.InfoType expectedInfoItemType() {
            return InfoItem.InfoType.CHANNEL;
        }

        @Disabled("Test not relevant here")
        @Override
        public void testMoreRelatedItems() throws Exception { }
    }

    private abstract static class AbstractSearchBaseTest extends DefaultSearchExtractorTest {

        protected static SearchExtractor extractor;
        protected static String query;
        protected static String expectedSearchBaseUrl;
        protected static String expectedSearchOriginalBaseUrl;

        @Override public SearchExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return Rumble; }
        @Override public String expectedName() { return query; }
        @Override public String expectedId() { return query; }
        @Override public String expectedUrlContains() throws UnsupportedEncodingException {
            return  expectedSearchBaseUrl + Utils.encodeUrlUtf8(query);
        }
        @Override public String expectedOriginalUrlContains() throws UnsupportedEncodingException {
            return expectedSearchOriginalBaseUrl  + Utils.encodeUrlUtf8(query);
        }
        @Override public String expectedSearchString() { return query; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
    }
}
