public class UserQuery {

    final StartingWebsite startingWebsite = new StartingWebsite();
    final CrawlingDepth crawlingDepth = new CrawlingDepth();
    final UserData userData = new UserData();

    public UserData getUserData() {
        printWelcome();

        userData.startingWebsite = getStartingWebsiteFromUser();
        userData.maxCrawlingDepth = getCrawlingDepthFromUser();

        return userData;
    }
    public void printWelcome() {
        System.out.println("\nWelcome to WebCrawler\n");
        System.out.println("Please enter a website to start and the depth of websiteNode to crawl.");
    }
    public String getStartingWebsiteFromUser() {
        return startingWebsite.getStartingWebsiteFromUser();
    }

    public int getCrawlingDepthFromUser() {
        return crawlingDepth.getCrawlingDepthFromUser();
    }

}