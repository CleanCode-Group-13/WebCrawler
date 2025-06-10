import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrawlingDispatcherTest {

    @Mock
    private UserData mockUserData;
    @Mock
    private JsoupDocumentFetcher mockDocumentFetcher;

    private CrawlingDispatcher spyCrawlingDispatcher;
    private java.util.Map<String, Website> websiteResponses;

    @BeforeEach
    void setUp() {
        websiteResponses = new java.util.HashMap<>();
        spyCrawlingDispatcher = spy(new CrawlingDispatcher(mockUserData, mockDocumentFetcher));

        lenient().doAnswer(invocation -> {
            String url = invocation.getArgument(0);
            WebCrawler mockWebCrawler = mock(WebCrawler.class);

            when(mockWebCrawler.getWebsiteHeadingsAndLinks()).thenReturn(websiteResponses.get(url));
            return mockWebCrawler;
        }).when(spyCrawlingDispatcher).createWebCrawler(anyString(), any(JsoupDocumentFetcher.class));
    }

    private Website createWebsite(String url, String... functionalLinks) {
        Website website = new Website();
        website.urlString = url;
        website.functionalLinks = new ArrayList<>(Arrays.asList(functionalLinks));
        website.headings = new ArrayList<>();
        website.brokenLinks = new ArrayList<>();
        return website;
    }
    
    @Test
    void testCrawlWeb_singlePageNoLinks() {
        mockUserData.startingWebsite = "http://example.com/start";
        mockUserData.maxCrawlingDepth = 1; 
        
        Website startWebsite = createWebsite("http://example.com/start");
        websiteResponses.put("http://example.com/start", startWebsite);
        
        spyCrawlingDispatcher.crawlWeb();
        
        assertNotNull(spyCrawlingDispatcher.getRootNode(), "Root node should not be null");
        assertEquals(startWebsite.urlString, spyCrawlingDispatcher.getRootNode().getWebsite().urlString, "Root node URL should match starting website");
        assertTrue(spyCrawlingDispatcher.getRootNode().getChildren().isEmpty(), "Root node should have no children as there are no links");
        
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/start", mockDocumentFetcher);
    }
    
    @Test
    void testCrawlWeb_withLinksAndDepthLimit() {
       
        mockUserData.startingWebsite = "http://example.com/start";
        mockUserData.maxCrawlingDepth = 2;
        
        Website startWebsite = createWebsite("http://example.com/start", "http://example.com/page1", "http://example.com/page2");
        Website page1 = createWebsite("http://example.com/page1", "http://example.com/page1_1", "http://example.com/page1_2"); 
        Website page2 = createWebsite("http://example.com/page2", "http://example.com/page2_1"); 
        Website page1_1 = createWebsite("http://example.com/page1_1");
        Website page1_2 = createWebsite("http://example.com/page1_2", "http://example.com/page1_2_1"); 
        Website page2_1 = createWebsite("http://example.com/page2_1"); 
        
        websiteResponses.put("http://example.com/start", startWebsite);
        websiteResponses.put("http://example.com/page1", page1);
        websiteResponses.put("http://example.com/page2", page2);
        websiteResponses.put("http://example.com/page1_1", page1_1);
        websiteResponses.put("http://example.com/page1_2", page1_2);
        websiteResponses.put("http://example.com/page2_1", page2_1);
        
        spyCrawlingDispatcher.crawlWeb();
        
        assertNotNull(spyCrawlingDispatcher.getRootNode(), "Root node should not be null");
        assertEquals("http://example.com/start", spyCrawlingDispatcher.getRootNode().getWebsite().urlString, "Root URL mismatch");
        
        List<WebsiteNode> depth1Children = spyCrawlingDispatcher.getRootNode().getChildren();
        assertEquals(2, depth1Children.size(), "Root node should have 2 children (page1, page2)");

        WebsiteNode page1Node = depth1Children.stream()
                .filter(node -> node.getWebsite() != null && "http://example.com/page1".equals(node.getWebsite().urlString))
                .findFirst().orElse(null);
        assertNotNull(page1Node, "Page1 node should exist at depth 1");

        WebsiteNode page2Node = depth1Children.stream()
                .filter(node -> node.getWebsite() != null && "http://example.com/page2".equals(node.getWebsite().urlString))
                .findFirst().orElse(null);
        assertNotNull(page2Node, "Page2 node should exist at depth 1");
        
        List<WebsiteNode> depth2ChildrenOfPage1 = page1Node.getChildren();
        assertEquals(2, depth2ChildrenOfPage1.size(), "Page1 should have 2 children (page1_1, page1_2)");

        WebsiteNode page1_1Node = depth2ChildrenOfPage1.stream()
                .filter(node -> node.getWebsite() != null && "http://example.com/page1_1".equals(node.getWebsite().urlString))
                .findFirst().orElse(null);
        assertNotNull(page1_1Node, "Page1_1 node should exist at depth 2");
        assertTrue(page1_1Node.getChildren().isEmpty(), "Page1_1 should have no children (max depth reached for next level)");

        WebsiteNode page1_2Node = depth2ChildrenOfPage1.stream()
                .filter(node -> node.getWebsite() != null && "http://example.com/page1_2".equals(node.getWebsite().urlString))
                .findFirst().orElse(null);
        assertNotNull(page1_2Node, "Page1_2 node should exist at depth 2");
        assertTrue(page1_2Node.getChildren().isEmpty(), "Page1_2 should have no children (max depth reached for next level)");
        
        List<WebsiteNode> depth2ChildrenOfPage2 = page2Node.getChildren();
        assertEquals(1, depth2ChildrenOfPage2.size(), "Page2 should have 1 child (page2_1)");

        WebsiteNode page2_1Node = depth2ChildrenOfPage2.stream()
                .filter(node -> node.getWebsite() != null && "http://example.com/page2_1".equals(node.getWebsite().urlString))
                .findFirst().orElse(null);
        assertNotNull(page2_1Node, "Page2_1 node should exist at depth 2");
        assertTrue(page2_1Node.getChildren().isEmpty(), "Page2_1 should have no children (max depth reached for next level)");
        
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/start", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/page1", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/page2", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/page1_1", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/page1_2", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/page2_1", mockDocumentFetcher);
        
        verify(spyCrawlingDispatcher, never()).createWebCrawler("http://example.com/page1_2_1", mockDocumentFetcher);
    }
    
    @Test
    void testCrawlWeb_handlesCircularReferences() {

        mockUserData.startingWebsite = "http://example.com/start";
        mockUserData.maxCrawlingDepth = 3; 
        
        Website startWebsite = createWebsite("http://example.com/start", "http://example.com/pageA");
        Website pageA = createWebsite("http://example.com/pageA", "http://example.com/pageB");
        Website pageB = createWebsite("http://example.com/pageB", "http://example.com/start"); 

        websiteResponses.put("http://example.com/start", startWebsite);
        websiteResponses.put("http://example.com/pageA", pageA);
        websiteResponses.put("http://example.com/pageB", pageB);
        
        spyCrawlingDispatcher.crawlWeb();
        
        assertNotNull(spyCrawlingDispatcher.getRootNode(), "Root node should not be null");
        assertEquals("http://example.com/start", spyCrawlingDispatcher.getRootNode().getWebsite().urlString, "Root URL mismatch");
        
        List<WebsiteNode> startChildren = spyCrawlingDispatcher.getRootNode().getChildren();
        assertEquals(1, startChildren.size(), "Start node should have 1 child (pageA)");
        WebsiteNode pageANode = startChildren.getFirst();
        assertEquals("http://example.com/pageA", pageANode.getWebsite().urlString, "PageA URL mismatch");
        
        List<WebsiteNode> pageAChildren = pageANode.getChildren();
        assertEquals(1, pageAChildren.size(), "PageA node should have 1 child (pageB)");
        WebsiteNode pageBNode = pageAChildren.getFirst();
        assertEquals("http://example.com/pageB", pageBNode.getWebsite().urlString, "PageB URL mismatch");
        
        assertTrue(pageBNode.getChildren().isEmpty(), "PageB should have no children due to circular reference detection");
        
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/start", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/pageA", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/pageB", mockDocumentFetcher);
    }
    
    @Test
    void testCrawlWeb_emptyStartingWebsite() {
        mockUserData.startingWebsite = "http://example.com/nonexistent";
        mockUserData.maxCrawlingDepth = 1;
        
        websiteResponses.put("http://example.com/nonexistent", null);
        
        spyCrawlingDispatcher.crawlWeb();
        
        assertNotNull(spyCrawlingDispatcher.getRootNode(), "Root node should still be initialized even if website is null");
        assertNull(spyCrawlingDispatcher.getRootNode().getWebsite(), "Root node's website should be null if start page not found");
        assertTrue(spyCrawlingDispatcher.getRootNode().getChildren().isEmpty(), "No children should be added if start page is null");
        
        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/nonexistent", mockDocumentFetcher);
    }
    
    @Test
    void testCrawlWeb_interruptionDuringCrawling() throws InterruptedException {
        mockUserData.startingWebsite = "http://example.com/start";
        mockUserData.maxCrawlingDepth = 2;
        
        Website startWebsite = createWebsite("http://example.com/start", "http://example.com/slowPage");
        Website slowPage = createWebsite("http://example.com/slowPage");

        websiteResponses.put("http://example.com/start", startWebsite);

        CountDownLatch latchForSlowPage = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            String url = invocation.getArgument(0);
            WebCrawler mockWebCrawler = mock(WebCrawler.class);

            if (url.equals("http://example.com/slowPage")) {
                when(mockWebCrawler.getWebsiteHeadingsAndLinks()).thenAnswer(webCrawlerInvocation -> {
                    System.out.println("Simulating slow page: Waiting for interruption or timeout...");
                    try {
                        latchForSlowPage.await(5, TimeUnit.SECONDS); 
                        System.out.println("Simulating slow page: Latch released/timed out.");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Simulating slow page: Interrupted while waiting. Returning null website.");
                        return null; 
                    }
                    return slowPage;
                });
            } else {
                when(mockWebCrawler.getWebsiteHeadingsAndLinks()).thenReturn(websiteResponses.get(url));
            }
            return mockWebCrawler;
        }).when(spyCrawlingDispatcher).createWebCrawler(anyString(), any(JsoupDocumentFetcher.class));

        Thread crawlingThread = getCrawlingThread();
        Thread.sleep(1000);

        System.out.println("Test thread: Interrupting crawlingThread...");
        crawlingThread.interrupt();
        crawlingThread.join(5000); // Wait up to 5 seconds

        System.out.println("Test thread: crawlingThread.isAlive() = " + crawlingThread.isAlive());
        assertFalse(crawlingThread.isAlive(), "Crawling thread should have terminated after interruption");

        verify(spyCrawlingDispatcher, times(1)).createWebCrawler("http://example.com/start", mockDocumentFetcher);
        verify(spyCrawlingDispatcher, atLeastOnce()).createWebCrawler("http://example.com/slowPage", mockDocumentFetcher);

        assertNotNull(spyCrawlingDispatcher.getRootNode(), "Root node should still be present");
        assertNotNull(spyCrawlingDispatcher.getRootNode().getWebsite(), "Start website should be set");
    }

    private Thread getCrawlingThread() {
        Thread crawlingThread = new Thread(() -> {
            try {
                System.out.println("Crawling thread: Starting crawlWeb()...");
                spyCrawlingDispatcher.crawlWeb();
                System.out.println("Crawling thread: crawlWeb() finished gracefully.");
            } catch (Exception e) {
                System.err.println("CrawlingDispatcher threw unexpected exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                ExceptionLogger.log(e);
            } finally {
                System.out.println("Crawling thread: Exiting run() method.");
            }
        });
        crawlingThread.start();
        return crawlingThread;
    }
}
