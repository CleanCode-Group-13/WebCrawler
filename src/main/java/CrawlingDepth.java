import java.util.Scanner;

public class CrawlingDepth {
    int crawlingDepth;

    public int getCrawlingDepthFromUser() {
        Scanner scanner = new Scanner(System.in);
        do {
            printPromptForCrawlingDepth();
            crawlingDepth = scanner.nextInt();
        } while (!isValidCrawlingDepth());
        return crawlingDepth;
    }

    private void printPromptForCrawlingDepth() {
        System.out.print("Please enter the crawling depth (max. 3): ");
    }

    @SuppressWarnings("RedundantIfStatement")
    protected boolean isValidCrawlingDepth() {
        if (crawlingDepth >= 1 && crawlingDepth <= 3) {
            return true;
        }
        return false;
    }
}