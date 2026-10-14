package org.schabi.newpipe.extractor.services.rumble.extractors;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderFactory;
import org.schabi.newpipe.extractor.ExtractorAsserts;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.BaseChannelExtractorTest;
import org.schabi.newpipe.extractor.services.DefaultTests;
import org.schabi.newpipe.extractor.services.youtube.YoutubeTestsUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.schabi.newpipe.downloader.DownloaderFactory.RESOURCE_PATH;
import static org.schabi.newpipe.extractor.ServiceList.Rumble;

@SuppressWarnings({"checkstyle:LineLength", "checkstyle:MethodName", "checkstyle:InvalidJavadocPosition"})
/**
 * Test for {@link RumbleChannelExtractor}
 */
public class RumbleChannelExtractorTest {

    public static class SinglePageChannel extends TestChannel {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.channelUrl, "https://rumble.com/c/GovRonDeSantis");
                put(KeysForTestDataMap.expectedOriginalUrl, "https://rumble.com/c/GovRonDeSantis");
                put(KeysForTestDataMap.expectedUrl, "https://rumble.com/c/GovRonDeSantis");
                put(KeysForTestDataMap.expectedChannelName, "Ron DeSantis");
                put(KeysForTestDataMap.expectedId, "c/GovRonDeSantis");
                put(KeysForTestDataMap.expectedMinSubscriberCount, "52000");
                put(KeysForTestDataMap.expectedDescription, "");
                put(KeysForTestDataMap.expectedAvatarUrl,
                        "https://sp.rmbl.ws/z8/7/o/2/c/7o2ca.baa-GovRonDeSantis-qrikr6.jpeg");
                put(KeysForTestDataMap.expectedBannerlUrl, "{\"hasBanner\":"
                        + /* ===expected to have banner? ===*/
                        "true"
                        + ",\"url\":\""
                        + /* ===set the expected url=== */
                        "https://sp.rmbl.ws/z8/7/o/2/c/7o2ca.caa-GovRonDeSantis-qrikr9.jpeg"
                        + "\"}");
                put(KeysForTestDataMap.doTestMoreRelatedItems, "false");
                put(KeysForTestDataMap.mockPath, "channelTest");
                put(KeysForTestDataMap.isVerified, "true");
            }};
            System.setProperty("downloader", "MOCK");
            // System.setProperty("downloader", "RECORDING");
            TestChannel.setUp();
            /** more info see: {@link RumbleSharedTests#infoItemsResultsTest} */
            someExpectedResults = new String[]{
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Ron DeSantis', textualUploadDate='2023-05-26T10:05:39-04:00', viewCount=16600, duration=1900, uploaderUrl='https://rumble.com/c/GovRonDeSantis', infoType=STREAM, serviceId=6, url='https://rumble.com/v2q5kfw-governor-desantis-speaks-at-the-florida-parent-educators-associations-homes.html', name='Governor DeSantis Speaks at the Florida Parent Educators Association’s Homeschool Convention', thumbnailUrl='https://sp.rmbl.ws/s8/6/8/I/5/0/8I50j.oq1b.jpg', uploaderVerified='false'}",
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Ron DeSantis', textualUploadDate='2023-05-25T11:18:06-04:00', viewCount=3210, duration=160, uploaderUrl='https://rumble.com/c/GovRonDeSantis', infoType=STREAM, serviceId=6, url='https://rumble.com/v2pyksy-governor-ron-desantis-signs-record-tax-relief-bill.html', name='Governor Ron DeSantis Signs Record Tax Relief Bill', thumbnailUrl='https://sp.rmbl.ws/s8/6/s/7/P/Z/s7PZj.oq1b.jpg', uploaderVerified='false'}",
            };
        }
    }

    public static class MultiplePageChannel extends TestChannel {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.channelUrl, "https://rumble.com/c/Bongino");
                put(KeysForTestDataMap.expectedOriginalUrl, "https://rumble.com/c/Bongino");
                put(KeysForTestDataMap.expectedUrl, "https://rumble.com/c/Bongino");
                put(KeysForTestDataMap.expectedChannelName, "The Dan Bongino Show");
                put(KeysForTestDataMap.expectedId, "c/Bongino");
                put(KeysForTestDataMap.expectedMinSubscriberCount, "1500000");
                put(KeysForTestDataMap.expectedDescription, "");
                put(KeysForTestDataMap.expectedAvatarUrl, "https://sp.rmbl.ws/z8/U/K/d/b/UKdba.baa.1-Bongino-rj8lv1.jpeg");
                put(KeysForTestDataMap.expectedBannerlUrl, "{\"hasBanner\":"
                        + /* ===expected to have banner? ===*/
                        "true"
                        + ",\"url\":\""
                        + /* ===set the expected url=== */
                        "https://sp.rmbl.ws/z8/U/K/d/b/UKdba.caa.4-Bongino-rqwkrs.jpeg"
                        + "\"}");
                put(KeysForTestDataMap.doTestMoreRelatedItems, "true");
                put(KeysForTestDataMap.mockPath, "channelTestMulitplePages");
                put(KeysForTestDataMap.isVerified, "true");
            }};
            System.setProperty("downloader", "MOCK");
            //System.setProperty("downloader", "RECORDING");
            TestChannel.setUp();
            /** more info see: {@link RumbleSharedTests#infoItemsResultsTest} */
            someExpectedResults = new String[]{
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='The Dan Bongino Show', textualUploadDate='2023-05-03T10:23:27-04:00', viewCount=947000, duration=3374, uploaderUrl='https://rumble.com/c/Bongino', infoType=STREAM, serviceId=6, url='https://rumble.com/v2lthos-the-tucker-fox-feud-is-getting-weird-ep.-2003-05032023.html', name='The Tucker, Fox Feud Is Getting Weird (Ep. 2003) - 05/03/2023', thumbnailUrl='https://sp.rmbl.ws/s8/1/m/V/h/z/mVhzj.oq1b.5-small-The-Tucker-Fox-Feud-Is-Gett.jpg', uploaderVerified='false'}",
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='The Dan Bongino Show', textualUploadDate='2023-05-02T10:16:43-04:00', viewCount=1250000, duration=3355, uploaderUrl='https://rumble.com/c/Bongino', infoType=STREAM, serviceId=6, url='https://rumble.com/v2lmte6-an-interesting-turn-in-the-tucker-story-ep.-2002-05022023.html', name='An Interesting Turn in the Tucker Story (Ep. 2002) - 05/02/2023', thumbnailUrl='https://sp.rmbl.ws/s8/1/o/T/7/x/oT7xj.oq1b.2-small-An-Interesting-Turn-in-the-.jpg', uploaderVerified='false'}",
            };
        }
    }
    public static class MultiplePageUser extends TestChannel {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.channelUrl, "https://rumble.com/user/Vlemx");
                put(KeysForTestDataMap.expectedOriginalUrl, "https://rumble.com/user/Vlemx");
                put(KeysForTestDataMap.expectedUrl, "https://rumble.com/user/Vlemx");
                put(KeysForTestDataMap.expectedChannelName, "Vlemx");
                put(KeysForTestDataMap.expectedId, "user/Vlemx");
                put(KeysForTestDataMap.expectedMinSubscriberCount, "915");
                put(KeysForTestDataMap.expectedDescription, "");
                put(KeysForTestDataMap.expectedAvatarUrl, "https://graph.facebook.com/10153436058551636/picture?type=large");
                put(KeysForTestDataMap.expectedBannerlUrl, "{\"hasBanner\":false,\"url\":\"\"}");
                put(KeysForTestDataMap.doTestMoreRelatedItems, "true");
                put(KeysForTestDataMap.mockPath, "userTestMulitplePages");
                put(KeysForTestDataMap.isVerified, "false");
            }};
            System.setProperty("downloader", "MOCK");
            //System.setProperty("downloader", "RECORDING");
            TestChannel.setUp();
            /** more info see: {@link RumbleSharedTests#infoItemsResultsTest} */
            someExpectedResults = new String[]{
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Vlemx', textualUploadDate='2023-05-01T11:21:05-04:00', viewCount=40, duration=91, uploaderUrl='https://rumble.com/user/Vlemx', infoType=STREAM, serviceId=6, url='https://rumble.com/v2lgf2i-from-transgender-to-transable-people-are-now-choosing-to-identify-as-handic.html', name='From Transgender to Transable: People are Now Choosing to Identify as Handicapped', thumbnailUrl='https://sp.rmbl.ws/s8/6/k/1/Y/w/k1Ywj.oq1b.1.jpg', uploaderVerified='false'}",
                    "StreamInfoItem{streamType=VIDEO_STREAM, uploaderName='Vlemx', textualUploadDate='2023-05-01T01:00:08-04:00', viewCount=47, duration=107, uploaderUrl='https://rumble.com/user/Vlemx', infoType=STREAM, serviceId=6, url='https://rumble.com/v2lds7a-nc-pregnant-woman-shot-7-times-outside-fayetteville-store-loses-unborn-chil.html', name='NC Pregnant woman shot 7 times outside Fayetteville store loses unborn child', thumbnailUrl='https://sp.rmbl.ws/s8/6/g/0/u/w/g0uwj.oq1b.jpg', uploaderVerified='false'}",
            };
        }
    }

    public abstract static class TestChannel implements BaseChannelExtractorTest {

        protected static final String MOCK_PATH =
                RESOURCE_PATH + "/services/rumble/extractor/channel/";
        public enum KeysForTestDataMap {
            channelUrl,
            expectedOriginalUrl,
            expectedUrl,
            expectedChannelName,
            expectedId,
            expectedMinSubscriberCount,
            expectedDescription,
            expectedAvatarUrl,
            expectedBannerlUrl,
            doTestMoreRelatedItems,
            mockPath,
            isVerified
        }

        protected static RumbleChannelExtractor extractor;
        protected static Map<KeysForTestDataMap, String> testDataMap;
        /** more info see: {@link RumbleSharedTests#infoItemsResultsTest} */
        protected static String[] someExpectedResults = null;

        public static void setUp() throws Exception {
            NewPipe.init(new DownloaderFactory().getDownloader(MOCK_PATH + "/"
                    + testDataMap.get(KeysForTestDataMap.mockPath)));
            extractor = (RumbleChannelExtractor) Rumble
                    .getChannelExtractor(testDataMap.get(KeysForTestDataMap.channelUrl));
            extractor.fetchPage();
        }

        /*//////////////////////////////////////////////////////////////////////////
        // Extractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testServiceId() {
            assertEquals(Rumble.getServiceId(), extractor.getServiceId());
        }

        @Test
        public void testName() throws Exception {
            assertEquals(testDataMap.get(KeysForTestDataMap.expectedChannelName), extractor.getName());
        }

        @Test
        public void testId() throws Exception {
            assertEquals(testDataMap.get(KeysForTestDataMap.expectedId), extractor.getId());
        }

        @Test
        public void testUrl() throws ParsingException {
            assertEquals(testDataMap.get(KeysForTestDataMap.expectedUrl), extractor.getUrl());
        }

        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals(testDataMap.get(KeysForTestDataMap.expectedOriginalUrl), extractor.getOriginalUrl());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ListExtractor
        //////////////////////////////////////////////////////////////////////////*/

        //@Test
        //public void testRelatedItems() throws Exception {
        //    defaultTestRelatedItems(extractor);

        //    infoItemsResultsTest(extractor.getService(),
        //            extractor.getInitialPage().getItems(),
        //            extractor.getInitialPage().getErrors(),
        //            someExpectedResults
        //    );
        //}

        //@Test
        //public void testMoreRelatedItems() throws Exception {
        //    if (Boolean.parseBoolean(testDataMap.get(KeysForTestDataMap.doTestMoreRelatedItems))) {
        //        defaultTestMoreItems(extractor);
        //    } else {
        //        assertTrue(true);
        //    }
        //}

        /*//////////////////////////////////////////////////////////////////////////
        // ChannelExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testDescription() throws Exception {
            ExtractorAsserts.assertContains(testDataMap.get(KeysForTestDataMap.expectedDescription), extractor.getDescription());
        }

        //@Test
        //public void testAvatarUrl() throws Exception {
        //    final String avatarUrl = extractor.getAvatarUrl();
        //    assertIsSecureUrl(avatarUrl);
        //    assertEquals(testDataMap.get(KeysForTestDataMap.expectedAvatarUrl), avatarUrl);
        //}

        //@Test
        //public void testBannerUrl() throws Exception {

        //    final String bannerUrl = extractor.getBannerUrl();
        //    final JsonObject testData = (JsonObject) JsonParser.any().from(testDataMap.get(KeysForTestDataMap.expectedBannerlUrl));

        //    final boolean hasBanner = testData.getBoolean("hasBanner");
        //    final String expectedBannerUrl = testData.getString("url");
        //    if (hasBanner) {
        //        assertIsSecureUrl(bannerUrl);
        //        assertEquals(expectedBannerUrl, bannerUrl);
        //    } else {
        //        assertEquals(null, bannerUrl);
        //    }
        //}

        @Test
        public void testFeedUrl() throws Exception {
            assertNull(extractor.getFeedUrl());
        }

        @Test
        public void testSubscriberCount() throws Exception {
            final long subscriberCount = Long.parseLong(testDataMap.get(KeysForTestDataMap.expectedMinSubscriberCount));
            assertTrue(extractor.getSubscriberCount() >= subscriberCount, "Wrong subscriber count");
        }

        @Override
        @Test
        public void testVerified() throws Exception {
            if (Boolean.parseBoolean(testDataMap.get(KeysForTestDataMap.isVerified))) {
                assertTrue(extractor.isVerified());
            } else {
                assertFalse(extractor.isVerified());
            }
        }

        @Test
        public void testAvatars() throws Exception {
            DefaultTests.defaultTestImageCollection(extractor.getAvatars());
            // assertEquals(testDataMap.get(KeysForTestDataMap.expectedAvatarUrl), avatarUrl);
        }

        @Override
        public void testBanners() throws Exception {
            //final String bannerUrl = extractor.getBannerUrl();
            //final JsonObject testData = (JsonObject) JsonParser.any().from(testDataMap.get(KeysForTestDataMap.expectedBannerlUrl));

            //final boolean hasBanner = testData.getBoolean("hasBanner");
            //final String expectedBannerUrl = testData.getString("url");
            //if (hasBanner) {
            //    assertIsSecureUrl(bannerUrl);
            //    assertEquals(expectedBannerUrl, bannerUrl);
            //} else {
            //    assertEquals(null, bannerUrl);
            //}
            YoutubeTestsUtils.testImages(extractor.getBanners());
        }

        @Override
        public void testTabs() throws Exception {

        }

        @Override
        public void testTags() throws Exception {

        }
    }
}
