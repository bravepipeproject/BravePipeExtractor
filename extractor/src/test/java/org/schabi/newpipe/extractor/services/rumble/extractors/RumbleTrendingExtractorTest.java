package org.schabi.newpipe.extractor.services.rumble.extractors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.BaseListExtractorTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.schabi.newpipe.extractor.ServiceList.Rumble;
import static org.schabi.newpipe.extractor.services.DefaultTests.assertNoMoreItems;
import static org.schabi.newpipe.extractor.services.DefaultTests.defaultTestMoreItems;
import static org.schabi.newpipe.extractor.services.DefaultTests.defaultTestRelatedItems;

/**
 * Tests for {@link RumbleTrendingExtractor}
 *
 * We have atm 5 different Kiosk categories:
 *<p>
 * - Live
 * - Editor Picks
 * - Trending
 * - Today's Battle Leaderboard Top 50
 * - Latest
 *<p>
 *  Here we have a base class called Trending that implements testing for all of those categories.
 *  For every category we have one derived test class.
 */
public class RumbleTrendingExtractorTest {

    public static class Live extends Trending {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.name, "Live");
                put(KeysForTestDataMap.id, "Live");
                put(KeysForTestDataMap.url, Rumble.getBaseUrl() + "/browse/live");
                put(KeysForTestDataMap.originalUrl, Rumble.getBaseUrl() + "browse/live");
            }};
            Trending.setUp();
        }
    }

    public static class EditorPicks extends Trending {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.name, "Editor Picks");
                put(KeysForTestDataMap.id, "Editor Picks");
                put(KeysForTestDataMap.url, Rumble.getBaseUrl() + "/editor-picks");
                put(KeysForTestDataMap.originalUrl, Rumble.getBaseUrl() + "/editor-picks");
            }};
            Trending.setUp();
        }
    }

    public static class Latest extends Trending {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.name, "Latest");
                put(KeysForTestDataMap.id, "Latest");
                put(KeysForTestDataMap.url, Rumble.getBaseUrl() + "/videos?date=this-week");
                put(KeysForTestDataMap.originalUrl, Rumble.getBaseUrl() + "/videos?date=this-week");
            }};
            Trending.setUp();
        }
    }

    public static class BattleLeaderboard extends Trending {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.name, "Today's Battle Leaderboard Top 50");
                put(KeysForTestDataMap.id, "Today's Battle Leaderboard Top 50");
                put(KeysForTestDataMap.url, Rumble.getBaseUrl() + "/battle-leaderboard");
                put(KeysForTestDataMap.originalUrl, Rumble.getBaseUrl() + "/battle-leaderboard");
            }};
            Trending.setUp();
        }

        @Override
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor);

        }
    }

    public static class TrendingToday extends Trending {
        @BeforeAll
        public static void setUp() throws Exception {
            testDataMap = new HashMap() {{
                put(KeysForTestDataMap.name, "Trending Today");
                put(KeysForTestDataMap.id, "Trending Today");
                put(KeysForTestDataMap.url, Rumble.getBaseUrl() + "/videos?sort=views&date=today");
                put(KeysForTestDataMap.originalUrl, Rumble.getBaseUrl()
                        + "/videos?sort=views&date=today");
            }};
            Trending.setUp();
        }
    }

    public abstract static class Trending implements BaseListExtractorTest {

        public enum KeysForTestDataMap {
            name, id, url, originalUrl
        }
        protected static ListExtractor extractor;
        protected static Map<KeysForTestDataMap, String> testDataMap;

        public static void setUp() throws Exception {
            NewPipe.init(DownloaderTestImpl.getInstance());

            extractor = Rumble.getKioskList().getExtractorById(
                    testDataMap.get(KeysForTestDataMap.id), null);
            extractor.fetchPage();
        }

        @AfterAll
        public static void tearDown() throws IOException, ExtractionException {
            final List<Throwable> errors = extractor.getInitialPage().getErrors();
            System.out.println(errors.toString());
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
            assertEquals(testDataMap.get(KeysForTestDataMap.name), extractor.getName());
        }

        @Test
        public void testId() throws Exception {
            assertEquals(testDataMap.get(KeysForTestDataMap.id), extractor.getId());
        }

        @Test
        public void testUrl() throws ParsingException {
            // TODO evermind figure out for what this URL really are
            //assertEquals(testDataMap.get(keysForTestDataMap.url), extractor.getUrl());
        }

        @Test
        public void testOriginalUrl() throws ParsingException {
            // TODO evermind figure out for what this URL really are
            // assertEquals(testDataMap.get(keysForTestDataMap.originalUrl),
            //         extractor.getOriginalUrl());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ListExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor);
        }

        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor);
        }
    }
}
