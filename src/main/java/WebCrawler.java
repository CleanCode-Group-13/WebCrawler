import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {
    private final String urlString;
    private JsoupDocumentFetcher jsoupDocFetcher;
    Document document;
    Website website;

    public WebCrawler(String urlString, JsoupDocumentFetcher jsoupDocFetcher) {
        this.urlString = urlString;
        this.jsoupDocFetcher = jsoupDocFetcher;
    }

    public Website getWebsiteHeadingsAndLinks() {
        website = new Website();
        website.urlString = urlString;
        try {
            document = jsoupDocFetcher.fetch(website.urlString);
            addHeadingsToWebsite();
            addLinksToWebsite();
        } catch (IOException e) {
            ExceptionLogger.log(e);
        }
        return website;
    }

    /**
    private Document getDocumentFromJsoup() throws IOException {
        return Jsoup.connect(website.urlString).get();
    }
     **/
    private void addHeadingsToWebsite() {
        for (int i = 0; i <= 6; i++) {
            Elements headings = document.select("h" + i);
            for (Element heading : headings) {
                website.headings.add("h" + i + " " + heading.text());
            }
        }
    }

    private void addLinksToWebsite() throws MalformedURLException {
        // remarks: we only need the host in this method, so we don't
        // make a class variable and shift the code of getting the host
        // to another method
        URL currentWebsiteUrl = new URL(website.urlString);
        String currentWebsiteHost = currentWebsiteUrl.getHost();

        Elements links = document.select("a[href]");
        for (Element link : links) {
            String urlString = link.absUrl("href");
            URL linkUrl = new URL(urlString);
            String linkHost = linkUrl.getHost();

            if (!linkHost.contains(currentWebsiteHost)) {
                addFunctionalAndBrokenLinks(urlString);
            }
        }
    }

    private void addFunctionalAndBrokenLinks(String urlString) {
        try {
            int statusCode = getStatusCodeFromJsoup(urlString);
            if (statusCode >= 200 && statusCode < 400) {
                website.functionalLinks.add(urlString);
            } else {
                website.brokenLinks.add(urlString);
            }
        } catch (IOException e) {
            website.brokenLinks.add(urlString);
            ExceptionLogger.log(e);
        }
    }

    private int getStatusCodeFromJsoup(String urlString) throws IOException {
        return Jsoup.connect(urlString)
                .ignoreHttpErrors(true)
                .method(org.jsoup.Connection.Method.HEAD)
                .execute()
                .statusCode();
    }
}
