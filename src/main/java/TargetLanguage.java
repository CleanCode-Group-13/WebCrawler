import com.deepl.api.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TargetLanguage {

    String targetLanguage;


    protected static String getTargetLanguageFromUser() {
        String targetLanguage;
        do {
            printPromptForTargetLanguage();
            String userInput = getUserInputLanguage();//original user input
            String formattedUserInput = getFormattedInputLanguage(userInput); //brings the string f.e. "CHInESE" to form "Chinese"
            String normalizedLanguageVariant = getNationalLanguageFormat(formattedUserInput);// converts to an API specific form f.e. "Chinese (simplified)"
            targetLanguage = normalizedLanguageVariant.trim();

        } while (!isValidTargetLanguage(targetLanguage));

        return targetLanguage;
    }

    protected static boolean isValidTargetLanguage(String targetLanguage) {
        DeeplAPIWrapper deeplAPIWrapper = new DeeplAPIWrapper();
        List<Language> supportedAPILanguages = deeplAPIWrapper.getSupportedLanguages();
        ArrayList <String> supportedLanguages = deeplAPIWrapper.getSupportedLanguageNamesList(supportedAPILanguages);

        return supportedLanguages.contains(targetLanguage);
    }

    private static void printPromptForTargetLanguage(){
        System.out.print("Please enter a target language: ");
    }



    protected static String getUserInputLanguage(){   //example output "English"
        Scanner scanner = new Scanner(System.in);
        String userInputLanguage = scanner.nextLine();
        return userInputLanguage;
    }


    public static String getFormattedInputLanguage (String language){
        String formattedInputLanguage = language.substring(0,1).toUpperCase()+language.substring(1).toLowerCase();
        return formattedInputLanguage;
    }

    //convertion is required by Deepl API
    protected static String getNationalLanguageFormat (String inputLanguageString){
        String formattedLanguageString;
        if(inputLanguageString.equals("English")){
            formattedLanguageString = "English "+"(British)";
        }else if(inputLanguageString.equals("Portuguese")){
            formattedLanguageString = "Portuguese "+"(European)";
        }else if(inputLanguageString.equals("Chinese")){
            formattedLanguageString = "Chinese "+"(simplified)";
        }else{
            formattedLanguageString = inputLanguageString;
        }
        return formattedLanguageString;
    }

}
