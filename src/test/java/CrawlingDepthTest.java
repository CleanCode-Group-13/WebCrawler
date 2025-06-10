import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingDepthTest {
    @Test
    void getCorrectCrawlingDepthFromUser() {
        InputStream originalSystemInput = System.in;
        String testInput = "1\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));

        try {
            CrawlingDepth crawlingDepth = new CrawlingDepth();
            int result = crawlingDepth.getCrawlingDepthFromUser();
            assertEquals(1, result);
        } finally {
            System.setIn(originalSystemInput);
        }
    }

    @Test
    void getCrawlingDepthFromUserReturnsInputValue() {
        InputStream originalSystemInput = System.in;
        String testInput = "3\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));

        try {
            CrawlingDepth crawlingDepth = new CrawlingDepth();
            int result = crawlingDepth.getCrawlingDepthFromUser();
            assertEquals(3, result);
        } finally {
            System.setIn(originalSystemInput);
        }
    }

    @Test
    void getCrawlingDepthHandlesNonIntegerInput() {
        InputStream originalSystemInput = System.in;
        String testInput = "abc\n1\n";
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));

        try {
            CrawlingDepth crawlingDepth = new CrawlingDepth();
            int result = crawlingDepth.getCrawlingDepthFromUser();
            assertEquals(1, result);
        } finally {
            System.setIn(originalSystemInput);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void validCrawlingDepthsAreAccepted(int depth) {
        CrawlingDepth crawlingDepth = new CrawlingDepth();
        crawlingDepth.crawlingDepth = depth;
        assertTrue(crawlingDepth.isValidCrawlingDepth());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 4, 99})
    void invalidCrawlingDepthsAreRejected(int depth) {
        CrawlingDepth crawlingDepth = new CrawlingDepth();
        crawlingDepth.crawlingDepth = depth;
        assertFalse(crawlingDepth.isValidCrawlingDepth());
    }
}
