import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class TranslationDispatcher {
    private DeeplAPIWrapper deeplAPIWrapper;
    private WebsiteNode rootNode;
    private String targetLanguage;

    public TranslationDispatcher(WebsiteNode rootNode, String targetLanguage) {
        this.rootNode = rootNode;
        this.targetLanguage = targetLanguage;
        this.deeplAPIWrapper = new DeeplAPIWrapper();
    }

    public void translateWebsiteNodes() {
        try {
            recursiveTranslate(rootNode, 0).get();
        } catch (InterruptedException | ExecutionException e) {
            ExceptionLogger.log(e);
        }
    }

    private String getTargetLanguageCode() {
        return deeplAPIWrapper.getLanguageCode(targetLanguage);
    }

    private CompletableFuture<Void> recursiveTranslate(WebsiteNode websiteNode, int depth) {
        if (websiteNode.getWebsite() == null) {
            return CompletableFuture.completedFuture(null); // Termination condition for recursion
        }

        ArrayList<String> headings = websiteNode.getWebsite().headings;

        // Create a list of CompletableFuture for concurrent translation
        List<CompletableFuture<Void>> translationFutures = new ArrayList<>();

        // Atomic index to maintain the order of translated headings
        AtomicInteger index = new AtomicInteger(0);

        for (String heading : headings) {
            if (!heading.isEmpty()) {
                String[] headingLevelAndHeading = heading.split(" ", 2);
                int headingLevel = Integer.parseInt(headingLevelAndHeading[0].substring(1));
                String headingToTranslate = headingLevelAndHeading[1];

                // Perform the translation asynchronously
                CompletableFuture<String> translationFuture = CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                return deeplAPIWrapper.getTranslatedHeading(headingToTranslate, getTargetLanguageCode());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

                // Combine the translated heading with the level and update the translatedHeadings list at the corresponding index
                CompletableFuture<Void> updateTranslationFuture = translationFuture.thenAccept(translatedHeading -> {
                    String translatedHeadingWithLevel = headingLevel + " " + translatedHeading;
                    int currentIndex = index.getAndIncrement();
                    websiteNode.getWebsite().translatedHeadings.add(currentIndex, translatedHeadingWithLevel);
                });

                translationFutures.add(updateTranslationFuture);
            }
        }

        // Recursive call to children
        List<CompletableFuture<Void>> childTranslationFutures = new ArrayList<>();
        for (WebsiteNode child : websiteNode.getChildren()) {
            CompletableFuture<Void> childTranslationFuture = recursiveTranslate(child, depth + 1);
            childTranslationFutures.add(childTranslationFuture);
        }

        // Combine all CompletableFuture for synchronization
        translationFutures.addAll(childTranslationFutures);
        return CompletableFuture.allOf(translationFutures.toArray(new CompletableFuture[0]));
    }
}





