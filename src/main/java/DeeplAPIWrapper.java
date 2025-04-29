import com.deepl.api.*;
import java.util.*;

public class DeeplAPIWrapper {

    private final String API_AUTH_KEY = "ecac6743-0147-be5d-5240-0fe60c397b08:fx";

    Translator translator;

    public DeeplAPIWrapper (){
        this.translator=new Translator(API_AUTH_KEY);
    }


    public List <Language> getSupportedLanguages(){
        List <Language> languageList = new ArrayList<>();
        try {
             languageList= translator.getTargetLanguages();

        }catch (DeepLException | InterruptedException e){
            ExceptionLogger.log(e);
        }
        return languageList;
    }

    //to check if the user´s input language is supported
    public ArrayList<String> getSupportedLanguageNamesList(List <Language> languageList){
        ArrayList<String> languageNamesList = new ArrayList<>();
        for(Language language:languageList){
            languageNamesList.add(language.getName());
        }
        return languageNamesList;
    }

    //return the code of the user´s input language
    public String getLanguageCode (String inputLanguage) {
        String languageCode=null;
        List <Language> languages = getSupportedLanguages();
        for(Language language: languages){
            if(language.getName().equals(inputLanguage)) {
               languageCode= language.getCode();
            }
        }
        return languageCode;
    }

    public String getTranslatedHeading (String heading, String targetLanguageCode) throws Exception {
        String translatedHeading = null;
        try {
            TextResult textResult = translator.translateText(heading, null, targetLanguageCode); //the source language is auto-detected
            translatedHeading = textResult.getText();

        } catch (DeepLException e) {
            ExceptionLogger.log(e);
        }
        return translatedHeading;
    }


}
