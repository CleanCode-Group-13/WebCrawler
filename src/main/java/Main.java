public class Main {

    public static void main(String[] args) throws Exception {
        UserData userData;
        WebsiteNode rootNode;

        UserQuery userQuery = new UserQuery();
        userData = userQuery.getUserData();

        CrawlingDispatcher crawlingDispatcher = new CrawlingDispatcher(userData);
        crawlingDispatcher.crawlWeb();
        rootNode = crawlingDispatcher.getRootNode();

        /**
        TranslationDispatcher translator = new TranslationDispatcher(rootNode, userData.targetLanguage);
        translator.translateWebsiteNodes();
         **/

        ResultProducer resultProducer = new ResultProducer(userData, rootNode);
        String mdString = resultProducer.makeMdString();

        FileGenerator fileGenerator = new FileGenerator();
        String mdFileName = "Web_Crawler_Report.md";
        fileGenerator.createMdFile(mdString, mdFileName);
    }

}