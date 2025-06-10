import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultProducerTest {

    @Test
    void printTestPage1() {
        UserData userData = new UserData();
        userData.startingWebsite = "https://javatpoint.com";
        userData.maxCrawlingDepth = 2;

        WebsiteNode websiteNode = new WebsiteNode();
        Website website = new Website();
        website.urlString = "https://google.com";
        website.headings.add("h1 Test");

        ResultProducer resultProducer = new ResultProducer(userData, websiteNode);

        String testString
                = "# Web Crawler Report"
                + "\nStarting Website: <a>"
                + userData.startingWebsite + "</a>"
                + "\nCrawling Depth: "
                + userData.maxCrawlingDepth
                + "\n\n";

        assertEquals(testString, resultProducer.makeMdString());

    }

}