import org.jsoup.nodes.Document;

import java.io.IOException;

public interface JsoupDocumentFetcher {
    Document fetch(String url) throws IOException;
}
