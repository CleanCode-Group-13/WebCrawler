import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebCrawlerTest {

    private JsoupDocumentFetcher fetcher;

    @BeforeEach
    public void setup() {
        fetcher = mock(JsoupDocumentFetcher.class);
    }

    @Test
    public void testGetWebsiteHeadingsAndLinks_addsHeadingsCorrectly() throws IOException {
        String url = "https://example.com";
        String html = "<html><body><h1>Welcome</h1><h2>Subheading</h2></body></html>";
        Document document = Jsoup.parse(html, url);

        when(fetcher.fetch(url)).thenReturn(document);

        WebCrawler crawler = new WebCrawler(url, fetcher);

        Website website = crawler.getWebsiteHeadingsAndLinks();

        assertEquals(2, website.headings.size());
        assertTrue(website.headings.contains("h1 Welcome"));
        assertTrue(website.headings.contains("h2 Subheading"));
    }

    @Test
    public void testGetWebsiteHeadingsAndLinks_addsNoLinksWhenNonePresent() throws IOException {
        String url = "https://example.com";
        String html = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html, url);

        when(fetcher.fetch(url)).thenReturn(document);

        WebCrawler crawler = new WebCrawler(url, fetcher);

        Website website = crawler.getWebsiteHeadingsAndLinks();

        assertTrue(website.functionalLinks.isEmpty());
        assertTrue(website.brokenLinks.isEmpty());
    }

    @Test
    public void testGetWebsiteHeadingsAndLinks_handlesIOExceptionGracefully() throws IOException {

        String url = "https://invalid.url";
        when(fetcher.fetch(url)).thenThrow(new IOException("Simulated exception"));

        WebCrawler crawler = new WebCrawler(url, fetcher);

        Website website = crawler.getWebsiteHeadingsAndLinks();

        assertNotNull(website);
        assertTrue(website.headings.isEmpty());
        assertTrue(website.functionalLinks.isEmpty());
        assertTrue(website.brokenLinks.isEmpty());
    }

    @Test
    public void testGetWebsiteHeadingsAndLinks_addsHeadingsAndLinksCorrectly() throws IOException {
        String url = "https://example.com";
        Document mockDocument = Jsoup.parse(
                "<html>" +
                        "<head><title>Test</title></head>" +
                        "<body>" +
                        "<h1>Main Heading</h1>" +
                        "<h2>Subheading</h2>" +
                        "<a href=\"https://google.com\">Good Link</a>" +
                        "<a href=\"https://external.com/bad\">Bad Link</a>" +
                        "</body>" +
                        "</html>"
        );

        when(fetcher.fetch(url)).thenReturn(mockDocument);

        WebCrawler crawler = new WebCrawler(url, fetcher);

        Website website = crawler.getWebsiteHeadingsAndLinks();

        assertEquals(url, website.urlString);

        assertTrue(website.headings.contains("h1 Main Heading"));
        assertTrue(website.headings.contains("h2 Subheading"));

        assertTrue(website.functionalLinks.contains("https://google.com"));
        assertTrue(website.brokenLinks.contains("https://external.com/bad"));

        assertEquals(1, website.functionalLinks.size());
        assertEquals(1, website.brokenLinks.size());
    }



}


