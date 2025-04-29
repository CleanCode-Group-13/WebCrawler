import com.deepl.api.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeeplAPIWrapperTest {

    DeeplAPIWrapper deeplAPIWrapper;
    ArrayList<String> supportedLanguagesNamesCheckList = new ArrayList<>(Arrays.asList("Arabic", "Bulgarian",
            "Czech", "Danish", "German", "Greek", "English (British)", "English (American)", "Spanish", "Estonian", "Finnish", "French",
            "Hungarian", "Indonesian", "Italian", "Japanese", "Korean", "Lithuanian", "Latvian", "Norwegian",
            "Dutch", "Polish", "Portuguese (Brazilian)", "Portuguese (European)", "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Turkish",
            "Ukrainian", "Chinese (simplified)", "Chinese (simplified)", "Chinese (traditional)"));

    @BeforeEach
    void setUp() {
        deeplAPIWrapper = new DeeplAPIWrapper();
    }

    @AfterEach
    void tearDown() {
        deeplAPIWrapper = null;
    }

    @Test
    void getLanguages() {
        List<Language> supportedLanguagesWithCodesList = deeplAPIWrapper.getSupportedLanguages();
        ArrayList<String> supportedLanguagesNamesList = new ArrayList<>();
        for (Language language : supportedLanguagesWithCodesList) {
            supportedLanguagesNamesList.add(language.getName());
        }
        Assertions.assertEquals(supportedLanguagesNamesList, supportedLanguagesNamesCheckList);
    }

    @Test
    void getLanguageNamesList(){
        ArrayList<String> supportedLanguagesNamesList = deeplAPIWrapper.getSupportedLanguageNamesList(deeplAPIWrapper.getSupportedLanguages());

        Assertions.assertEquals(supportedLanguagesNamesCheckList, supportedLanguagesNamesList);
    }

    @Test
    void getLanguageCode() {
        String apiCodeSpanish = deeplAPIWrapper.getLanguageCode("Spanish");
        String apiCodePortuguese = deeplAPIWrapper.getLanguageCode("Portuguese (Brazilian)");
        String apiCodePolish = deeplAPIWrapper.getLanguageCode("Polish");
        String apiCodeChinese = deeplAPIWrapper.getLanguageCode("Chinese (simplified)");

        Assertions.assertEquals("es", apiCodeSpanish);
        Assertions.assertEquals("pt-BR", apiCodePortuguese);
        Assertions.assertEquals("pl", apiCodePolish);
        Assertions.assertEquals("zh-HANS", apiCodeChinese);
    }

    @Test
    void getTranslatedHeading() throws Exception {
        String headingEnglish = "Hello";

        String expectedTranslationItalian = "Ciao";
        String expectedTranslationFrench = "Bonjour";
        String expectedTranslationGerman = "Hallo";

        Assertions.assertEquals(expectedTranslationItalian, deeplAPIWrapper.getTranslatedHeading(headingEnglish, "it"));
        Assertions.assertEquals(expectedTranslationFrench, deeplAPIWrapper.getTranslatedHeading(headingEnglish, "fr"));
        Assertions.assertEquals(expectedTranslationGerman, deeplAPIWrapper.getTranslatedHeading(headingEnglish, "de"));

    }


}
