public class UserQuery {
    final StartingWebsite startingWebsite = new StartingWebsite();
    final CrawlingDepth crawlingDepth = new CrawlingDepth();
    //final TargetLanguage targetLanguage = new TargetLanguage();
    final UserData userData = new UserData();


    public UserData getUserData() {

        printWelcome();

        userData.startingWebsite = getStartingWebsiteFromUser();
        userData.maxCrawlingDepth = getCrawlingDepthFromUser();
        //userData.targetLanguage = getTargetLanguageFromUser();

        // summaryOfUserInput(); // todo: side-effect, delete later

        return userData;
    }
    public void printWelcome() {
        System.out.println("\nWelcome to WebCrawler\n");
        System.out.println("Please enter a website to start and ");
        System.out.println("the depth of websiteNode to crawl");
    }
    public String getStartingWebsiteFromUser() {
        return startingWebsite.getStartingWebsiteFromUser();
    }

    public int getCrawlingDepthFromUser() {
        return crawlingDepth.getCrawlingDepthFromUser();
    }

/**
    public String getTargetLanguageFromUser() {
        return targetLanguage.getTargetLanguageFromUser();
    }**/

/**
protected String summaryOfUserInput() {
         return "\nStarting website: " + userData.startingWebsite +
                ", crawling depth: " + userData.maxCrawlingDepth;
    }
 **/
}