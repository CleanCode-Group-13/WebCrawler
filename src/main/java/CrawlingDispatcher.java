import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class CrawlingDispatcher {
    private UserData userData;
    private WebsiteNode rootNode;
    private Set<String> crawledUrls = Collections.synchronizedSet(new HashSet<>());
    private JsoupDocumentFetcher jsoupDocumentFetcher;
    private static final int THREAD_POOL_SIZE = 15; // works the best for my system
    private ExecutorService executorService;

    public CrawlingDispatcher(UserData userData, JsoupDocumentFetcher jsoupDocumentFetcher) {
        this.userData = userData;
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.jsoupDocumentFetcher = jsoupDocumentFetcher;
    }

    protected WebCrawler createWebCrawler(String url, JsoupDocumentFetcher fetcher) {
        return new WebCrawler(url, fetcher);
    }

    public void crawlWeb() {
        WebCrawler webCrawler = createWebCrawler(userData.startingWebsite, jsoupDocumentFetcher);
        Website website = webCrawler.getWebsiteHeadingsAndLinks();
        rootNode = new WebsiteNode();
        rootNode.setWebsite(website);
        crawledUrls.add(userData.startingWebsite);

        if (website != null) {
            CompletableFuture<Void> allCrawlTasks = crawlRecursivelyAndReturnFuture(rootNode, 1);
            allCrawlTasks.join();
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Crawling was interrupted..");
        }
        System.out.println("All links were successfully crawled. Report is being generated...");
    }

    private CompletableFuture<Void> crawlRecursivelyAndReturnFuture(WebsiteNode websiteNode, int currentCrawlingDepth) {
        if (currentCrawlingDepth > userData.maxCrawlingDepth || websiteNode.getWebsite() == null) {
            return CompletableFuture.completedFuture(null);
        }
        ArrayList<String> links = websiteNode.getWebsite().functionalLinks;
        List<CompletableFuture<Void>> childCrawlFutures = new ArrayList<>();

        for (String link : links) {
            if (!crawledUrls.contains(link)) {
                crawledUrls.add(link);

                CompletableFuture<Void> linkCrawlFuture = CompletableFuture.supplyAsync(() -> {
                    WebCrawler webCrawler = createWebCrawler(link, jsoupDocumentFetcher);
                    Website website = webCrawler.getWebsiteHeadingsAndLinks();
                    WebsiteNode childNode = new WebsiteNode();
                    childNode.setWebsite(website);
                    return childNode;
                }, executorService).thenCompose(childNode -> {
                    if (childNode.getWebsite() != null) {
                        websiteNode.addChild(childNode);
                        return crawlRecursivelyAndReturnFuture(childNode, currentCrawlingDepth + 1);
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                });
                childCrawlFutures.add(linkCrawlFuture);
            }
        }
        return CompletableFuture.allOf(childCrawlFutures.toArray(new CompletableFuture[0]));
    }

    public WebsiteNode getRootNode() {
        return rootNode;
    }
}


