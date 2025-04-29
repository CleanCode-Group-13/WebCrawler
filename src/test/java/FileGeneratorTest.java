import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileGeneratorTest {
    private FileGenerator fileGenerator;

    @BeforeEach
    public void setUp() {
        fileGenerator = new FileGenerator();
    }

    @AfterEach
    public void tearDown() {
        fileGenerator = null;
    }

    @Test
    public void testCreateMdFile(@TempDir Path tempDir) {
        String mdString = "# This is a test markdown file";
        String mdFileName = "test.md";

        Path mdFilePath = tempDir.resolve(mdFileName);
        fileGenerator.createMdFile(mdString, mdFilePath.toString());

        File createdFile = mdFilePath.toFile();
        assertTrue(createdFile.exists(), "The file should have been created");

        try (BufferedReader reader = new BufferedReader(new FileReader(createdFile))) {
            String firstLine = reader.readLine();
            assertEquals(mdString, firstLine, "The content of the file should match the provided string");
        } catch (IOException e) {
            throw new AssertionError("Error reading the created file", e);
        }
    }
}


