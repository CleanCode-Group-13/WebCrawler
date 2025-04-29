import java.util.ArrayList;

public class WebsiteNode {
    private Website website;
    private final ArrayList<WebsiteNode> children = new ArrayList<>();

    public void addChild(WebsiteNode child) {
        children.add(child);
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public ArrayList<WebsiteNode> getChildren() {
        return children;
    }
}
