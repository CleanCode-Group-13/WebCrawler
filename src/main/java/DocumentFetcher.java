import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentFetcher implements JsoupDocumentFetcher {
    public Document fetch(String url) throws IOException{
        return Jsoup.connect(url).get();
    }
}
