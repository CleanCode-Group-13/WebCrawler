import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class StartingWebsiteTest {

    @Test
    void getValidStartingWebsiteFromUser() {
        String testInput = "javatpoint.com\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(testInput.getBytes()));

        StartingWebsite startingWebsite = new StartingWebsite() {
            @Override
            public boolean isValidWebsite() {
                return true;
            }
        };

        String url = startingWebsite.getStartingWebsiteFromUser(scanner);
        assertEquals("https://javatpoint.com", url);
    }


    @Test
    void getInvalidStartingWebsiteFromUser() {
        InputStream originalSystemInput = System.in;
        String testWebsite = "https://www.google.com\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testWebsite.getBytes());
        System.setIn(byteArrayInputStream);

        StartingWebsite startingWebsite = new StartingWebsite();
        String url = startingWebsite.getStartingWebsiteFromUser();
        assertNotEquals("https://javatpoint.com", url);

        System.setIn(originalSystemInput);
    }

    @Test
    void prependHttpsNecessaryTest() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "www.google.com";
        startingWebsite.prependHttpsIfNecessary();
        assertEquals("https://www.google.com", startingWebsite.startingUrl);
    }

    @Test
    void prependHttpsNotNecessaryTest() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://www.google.com";
        startingWebsite.prependHttpsIfNecessary();
        assertEquals("https://www.google.com", startingWebsite.startingUrl);
    }
    @Test
    void isValidWebsiteTest() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://www.google.com";
        boolean shouldBeValidWebsite = startingWebsite.isValidWebsite();
        assertTrue(shouldBeValidWebsite);
    }

    @Test
    void isNotValidWebsiteTest1() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "";
        boolean shouldNotBeValidWebsite = startingWebsite.isValidWebsite();
        assertFalse(shouldNotBeValidWebsite);
    }

    @Test
    void isNotValidWebsiteTest2() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://";
        boolean shouldNotBeValidWebsite = startingWebsite.isValidWebsite();
        assertFalse(shouldNotBeValidWebsite);
    }

    @Test
    void isNotValidWebsiteTest3() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://hello";
        boolean shouldNotBeValidWebsite = startingWebsite.isValidWebsite();
        assertFalse(shouldNotBeValidWebsite);
    }

    @Test
    void isNotValidWebsiteTest4() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://at";
        boolean shouldNotBeValidWebsite = startingWebsite.isValidWebsite();
        assertFalse(shouldNotBeValidWebsite);
    }

    @Test
    void isNotValidWebsiteTest5() {
        StartingWebsite startingWebsite = new StartingWebsite();
        startingWebsite.startingUrl = "https://||9";
        boolean shouldNotBeValidWebsite = startingWebsite.isValidWebsite();
        assertFalse(shouldNotBeValidWebsite);
    }
}