package org.schabi.newpipe.extractor.services.rumble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabs;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.services.DefaultListExtractorTest;
import org.schabi.newpipe.extractor.services.rumble.extractors.RumbleChannelTabExtractor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import static org.schabi.newpipe.extractor.ServiceList.Rumble;
import static org.schabi.newpipe.extractor.services.DefaultTests.defaultTestGetPageInNewExtractor;

class RumbleChannelTabExtractorTest {

    static class All extends DefaultListExtractorTest<ChannelTabExtractor> {
        private static RumbleChannelTabExtractor extractor;

        @BeforeAll
        static void setUp() throws IOException, ExtractionException {
            NewPipe.init(DownloaderTestImpl.getInstance());
            extractor = (RumbleChannelTabExtractor) Rumble
                    .getChannelTabExtractorFromId("c/Bongino", ChannelTabs.VIDEOS);
            extractor.fetchPage();
        }

        @Override public ChannelTabExtractor extractor() throws Exception { return extractor; }
        @Override public StreamingService expectedService() throws Exception { return Rumble; }
        @Override public String expectedName() throws Exception { return ChannelTabs.VIDEOS.getNameId().name(); }
        @Override public String expectedId() throws Exception { return "c/Bongino"; }
        @Override public String expectedUrlContains() throws Exception { return "https://rumble.com/c/Bongino"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://rumble.com/c/Bongino"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        @Override public boolean expectedHasMoreItems() { return true; }

        @Test
        void testGetPageInNewExtractor() throws Exception {
            final ChannelTabExtractor newTabExtractor =
                    Rumble.getChannelTabExtractorFromId("c/Bongino", ChannelTabs.VIDEOS);
            defaultTestGetPageInNewExtractor(extractor, newTabExtractor);
        }
    }

    static class Videos extends DefaultListExtractorTest<ChannelTabExtractor> {
        private static RumbleChannelTabExtractor extractor;

        @BeforeAll
        static void setUp() throws IOException, ExtractionException {
            NewPipe.init(DownloaderTestImpl.getInstance());
            extractor = (RumbleChannelTabExtractor) Rumble
                    .getChannelTabExtractorFromId("c/Bongino/videos", ChannelTabs.VIDEOS);
            extractor.fetchPage();
        }

        @Override public ChannelTabExtractor extractor() throws Exception { return extractor; }
        @Override public StreamingService expectedService() throws Exception { return Rumble; }
        @Override public String expectedName() throws Exception { return ChannelTabs.VIDEOS.getNameId().name(); }
        @Override public String expectedId() throws Exception { return "c/Bongino/videos"; }
        @Override public String expectedUrlContains() throws Exception { return "https://rumble.com/c/Bongino/videos"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://rumble.com/c/Bongino/videos"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        @Override public boolean expectedHasMoreItems() { return true; }

        @Test
        void testGetPageInNewExtractor() throws Exception {
            final ChannelTabExtractor newTabExtractor =
                    Rumble.getChannelTabExtractorFromId("c/Bongino/videos", ChannelTabs.VIDEOS);
            defaultTestGetPageInNewExtractor(extractor, newTabExtractor);
        }
    }

    static class Live extends DefaultListExtractorTest<ChannelTabExtractor> {
        private static RumbleChannelTabExtractor extractor;

        @BeforeAll
        static void setUp() throws IOException, ExtractionException {
            NewPipe.init(DownloaderTestImpl.getInstance());
            extractor = (RumbleChannelTabExtractor) Rumble
                    .getChannelTabExtractorFromId("c/Bongino/livestreams", ChannelTabs.LIVESTREAMS);
            extractor.fetchPage();
        }

        @Override public ChannelTabExtractor extractor() throws Exception { return extractor; }
        @Override public StreamingService expectedService() throws Exception { return Rumble; }
        @Override public String expectedName() throws Exception { return ChannelTabs.LIVESTREAMS.getNameId().name(); }
        @Override public String expectedId() throws Exception { return "c/Bongino/livestreams"; }
        @Override public String expectedUrlContains() throws Exception { return "https://rumble.com/c/Bongino/livestreams"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://rumble.com/c/Bongino/livestreams"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        @Override public boolean expectedHasMoreItems() { return true; }

        @Test
        void testGetPageInNewExtractor() throws Exception {
            final ChannelTabExtractor newTabExtractor =
                    Rumble.getChannelTabExtractorFromId("c/Bongino/livestreams", ChannelTabs.LIVESTREAMS);
            defaultTestGetPageInNewExtractor(extractor, newTabExtractor);
        }
    }

    @Test
    void swagger() throws Exception {
        final OkHttpClient client = new OkHttpClient.Builder()
                .dns(hostname -> {
                    if (hostname.equals("web.de")) {
                        hostname = "gmx.com";
                    }
                    return Dns.SYSTEM.lookup(hostname);
                })
                .hostnameVerifier((hostname, session) -> {
                    if (hostname.equals("web.de")) {
                        return true;
                    }
                    return HttpsURLConnection
                            .getDefaultHostnameVerifier().verify(hostname, session);
                }).build();

//        final HttpUrl url = new HttpUrl.Builder()
//                .scheme("https")
//                //.host("api.example.com") don't use this host
//                .host("proxy.example.com") // use the one in the host header
//      .build();

//  Request okhttprequest = new Request.Builder()
//
//                .url(url)
//                .method("GET", )
//                //.header("Host", "proxy.example.com") don't need anymore
//                .build();
//
        final org.schabi.newpipe.extractor.downloader.Request request
                = new org.schabi.newpipe.extractor.downloader.Request(
                "GET",
                "https://web.de",
                null,
                null,
                null,
                false
        );
    execute(request, client);

    }
    public Response execute(@Nonnull final org.schabi.newpipe.extractor.downloader.Request request,
                            final OkHttpClient client)
            throws IOException, ReCaptchaException {
        final String httpMethod = request.httpMethod();
        final String url = request.url();
        final Map<String, List<String>> headers = request.headers();
        final byte[] dataToSend = request.dataToSend();

        RequestBody requestBody = null;
        if (dataToSend != null) {
            requestBody = RequestBody.create(null, dataToSend);
        }

        final okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                .method(httpMethod, requestBody).url(url);


        for (final Map.Entry<String, List<String>> pair : headers.entrySet()) {
            final String headerName = pair.getKey();
            final List<String> headerValueList = pair.getValue();

            if (headerValueList.size() > 1) {
                requestBuilder.removeHeader(headerName);
                for (final String headerValue : headerValueList) {
                    requestBuilder.addHeader(headerName, headerValue);
                }
            } else if (headerValueList.size() == 1) {
                requestBuilder.header(headerName, headerValueList.get(0));
            }

        }

        final OkHttpClient tmpClient = client;
        final okhttp3.Response response;

        response = tmpClient.newCall(requestBuilder.build()).execute();

        if (response.code() == 429) {
            response.close();

            throw new ReCaptchaException("reCaptcha Challenge requested", url);
        }

        final ResponseBody body = response.body();
        String responseBodyToReturn = null;

        if (body != null) {
            responseBodyToReturn = body.string();
        }

        final String latestUrl = response.request().url().toString();
        return new Response(response.code(), response.message(), response.headers().toMultimap(),
                responseBodyToReturn, latestUrl);
    }

    @Test
    void swagger2() throws Exception {
        HostSelectionInterceptor.brainiac();

    }
}
