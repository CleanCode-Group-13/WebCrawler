import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {
    public void createMdFile(String mdString, String mdFileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mdFileName))) {
            writer.write(mdString);
            System.out.println("This .md File has been created: " + mdFileName);
        } catch (IOException e) {
            System.err.println("Error writing the .md File\n" + e.getMessage());
        }
    }
}
