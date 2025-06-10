import java.util.Scanner;
public class CrawlingDepth {
    int crawlingDepth;
    public int getCrawlingDepthFromUser() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printPromptForCrawlingDepth();
            if (scanner.hasNextInt()) {
                crawlingDepth = scanner.nextInt();
                if (isValidCrawlingDepth()) {
                    return crawlingDepth;
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }
    private void printPromptForCrawlingDepth() {
        System.out.print("Please enter the crawling depth (max. 3): ");
    }
    protected boolean isValidCrawlingDepth() {
        return crawlingDepth >= 1 && crawlingDepth <= 3;
    }
}
