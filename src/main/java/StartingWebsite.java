import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class StartingWebsite {

    protected String startingUrl;

    public String getStartingWebsiteFromUser() {
        return getStartingWebsiteFromUser(new Scanner(System.in));
    }

    public String getStartingWebsiteFromUser(Scanner scanner) {
        do {
            printPromptForStartingWebsite();
            startingUrl = scanner.nextLine();
            prependHttpsIfNecessary();
        } while (!isValidWebsite());
        return startingUrl;
    }

    private void printPromptForStartingWebsite() {
        System.out.print("Please enter a starting website: ");
    }

    protected void prependHttpsIfNecessary() {
        if (!startingUrl.startsWith("https://")) {
            startingUrl = "https://" + startingUrl;
        }
    }

    public boolean isValidWebsite() {
        try {
            URL url = new URL(startingUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            connection.disconnect();

            // 200 <= responseCode < 400 indicates a successful connection
            return (responseCode >= 200 && responseCode < 400);
        } catch (MalformedURLException e) {
            // Invalid URL format
            System.out.println("This URL is malformed: " + e.getMessage());
            return false;
        } catch (IOException e) {
            // Error connecting to the URL
            System.out.println("There was an error connecting to the URL: " + e.getMessage());
            return false;
        }
    }
}