public class Main {

    public static void main(String[] args) throws Exception {
        UserData userData;
        WebsiteNode rootNode;
        JsoupDocumentFetcher fetcher;

        UserQuery userQuery = new UserQuery();
        userData = userQuery.getUserData();
        fetcher = new DocumentFetcher();

        CrawlingDispatcher crawlingDispatcher = new CrawlingDispatcher(userData, fetcher);
        crawlingDispatcher.crawlWeb();
        rootNode = crawlingDispatcher.getRootNode();

        ResultProducer resultProducer = new ResultProducer(userData, rootNode);
        String mdString = resultProducer.makeMdString();

        FileGenerator fileGenerator = new FileGenerator();
        String mdFileName = "Web_Crawler_Report.md";
        fileGenerator.createMdFile(mdString, mdFileName);
    }

}