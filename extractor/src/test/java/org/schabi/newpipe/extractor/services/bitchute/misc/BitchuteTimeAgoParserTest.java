package org.schabi.newpipe.extractor.services.bitchute.misc;

import org.junit.jupiter.api.Test;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitchuteTimeAgoParserTest {

    @Test
    @SuppressWarnings("checkstyle:LineLength")
    public void extractUploadDate()  throws ExtractionException {
        final BitchuteTimeAgoParser timeAgo = new BitchuteTimeAgoParser();

        // set to a known Date for testing
        final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-12-31T22:30:00+00:00");
        timeAgo.setOffsetDateTime(offsetDateTime);
        final String pattern1 = "2 years, 9 months ago"; // whitespace is here 0xa0
        final String pattern2 = "10 months, 3 weeks ago";
        final String pattern3 = "8 day, 5 hours ago";
        final String pattern4 = "2 hour ago";
        final String pattern5 = "1 year, 3 months ago";
        final String pattern6 = "2 years ago";
        final String weiredPattern = "an hour ago";
        final String minutesAgoPattern = "35 minutes ago";
        final String secondsAgoPattern = "16 seconds ago";

        final OffsetDateTime pattern1OffsetDateTimeExpected = OffsetDateTime.parse("2018-03-30T22:00:00+00:00");
        final OffsetDateTime pattern2OffsetDateTimeExpected = OffsetDateTime.parse("2020-02-08T22:00:00+00:00");
        final OffsetDateTime pattern3OffsetDateTimeExpected = OffsetDateTime.parse("2020-12-23T17:00:00+00:00");
        final OffsetDateTime pattern4OffsetDateTimeExpected = OffsetDateTime.parse("2020-12-31T20:30:00+00:00");
        final OffsetDateTime pattern5OffsetDateTimeExpected = OffsetDateTime.parse("2019-09-30T22:00:00+00:00");
        final OffsetDateTime pattern6OffsetDateTimeExpected = OffsetDateTime.parse("2018-12-30T22:00:00+00:00");
        final OffsetDateTime weiredPatternOffsetDateTimeExpected = OffsetDateTime.parse("2020-12-31T21:30:00+00:00");
        final OffsetDateTime minutesAgoPatternOffsetDateTimeExpected = OffsetDateTime.parse("2020-12-31T21:55:00+00:00");
        final OffsetDateTime secondsAgoPatternOffsetDateTimeExpected = OffsetDateTime.parse("2020-12-31T22:29:44+00:00");

        final OffsetDateTime pattern1OffsetDateTime = timeAgo.parse(pattern1).offsetDateTime();
        final OffsetDateTime pattern2OffsetDateTime = timeAgo.parse(pattern2).offsetDateTime();
        final OffsetDateTime pattern3OffsetDateTime = timeAgo.parse(pattern3).offsetDateTime();
        final OffsetDateTime pattern4OffsetDateTime = timeAgo.parse(pattern4).offsetDateTime();
        final OffsetDateTime pattern5OffsetDateTime = timeAgo.parse(pattern5).offsetDateTime();
        final OffsetDateTime pattern6OffsetDateTime = timeAgo.parse(pattern6).offsetDateTime();
        final OffsetDateTime weiredPatternOffsetDateTime = timeAgo.parse(weiredPattern).offsetDateTime();
        final OffsetDateTime minutesAgoPatternOffsetDateTime = timeAgo.parse(minutesAgoPattern).offsetDateTime();
        final OffsetDateTime secondsAgoPatternOffsetDateTime = timeAgo.parse(secondsAgoPattern).offsetDateTime();

        assertEquals(pattern1OffsetDateTimeExpected, pattern1OffsetDateTime);
        assertEquals(pattern2OffsetDateTimeExpected, pattern2OffsetDateTime);
        assertEquals(pattern3OffsetDateTimeExpected, pattern3OffsetDateTime);
        assertEquals(pattern4OffsetDateTimeExpected, pattern4OffsetDateTime);
        assertEquals(pattern5OffsetDateTimeExpected, pattern5OffsetDateTime);
        assertEquals(pattern6OffsetDateTimeExpected, pattern6OffsetDateTime);
        assertEquals(weiredPatternOffsetDateTimeExpected, weiredPatternOffsetDateTime);
        assertEquals(minutesAgoPatternOffsetDateTimeExpected, minutesAgoPatternOffsetDateTime);
        assertEquals(secondsAgoPatternOffsetDateTimeExpected, secondsAgoPatternOffsetDateTime);
    }
}
