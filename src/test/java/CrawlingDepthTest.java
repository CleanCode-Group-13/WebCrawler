import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingDepthTest {

    @Test
    void getCorrectCrawlingDepthFromUser() {
        InputStream originalSystemInput = System.in;
        String testCrawlingDepth = "1\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testCrawlingDepth.getBytes());
        System.setIn(byteArrayInputStream);

        CrawlingDepth crawlingDepth = new CrawlingDepth();
        int crawlingDepthFromUser = crawlingDepth.getCrawlingDepthFromUser();
        assertEquals(1, crawlingDepthFromUser);

        System.setIn(originalSystemInput);
    }

    @Test
    void getIncorrectCrawlingDepthFromUser() {
        InputStream originalSystemInput = System.in;
        String testCrawlingDepth = "3\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testCrawlingDepth.getBytes());
        System.setIn(byteArrayInputStream);

        CrawlingDepth crawlingDepth = new CrawlingDepth();
        int crawlingDepthFromUser = crawlingDepth.getCrawlingDepthFromUser();
        assertNotEquals(1, crawlingDepthFromUser);

        System.setIn(originalSystemInput);
    }

    @Test
    void isValidCrawlingDepth() {
        CrawlingDepth crawlingDepth = new CrawlingDepth();

        crawlingDepth.crawlingDepth = 1;
        assertTrue(crawlingDepth.isValidCrawlingDepth());
        crawlingDepth.crawlingDepth = 2;
        assertTrue(crawlingDepth.isValidCrawlingDepth());
        crawlingDepth.crawlingDepth = 3;
        assertTrue(crawlingDepth.isValidCrawlingDepth());

        crawlingDepth.crawlingDepth = 0;
        assertFalse(crawlingDepth.isValidCrawlingDepth());
        crawlingDepth.crawlingDepth = 4;
        assertFalse(crawlingDepth.isValidCrawlingDepth());
        crawlingDepth.crawlingDepth = -1;
        assertFalse(crawlingDepth.isValidCrawlingDepth());
    }
}