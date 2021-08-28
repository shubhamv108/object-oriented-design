package commons.writers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterTest {

    private FileWriter fileWriter;
    private final String filePath = "src/test/resources/testFile";
    private final Path path = Paths.get(filePath);

    @BeforeEach
    void setUp() throws IOException {
        this.fileWriter = FileWriter.of(filePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (path.toFile().exists())
            Files.delete(Path.of(filePath));
    }

    @Test
    void appendAll() throws IOException {
        List<String> inputLines = Arrays.asList("Line1", "Line2");
        fileWriter.appendAll(inputLines);
        fileWriter.flush();
        List<String> lines = Files.readAllLines(this.path);
        int i = 0;
        for (String line : lines)
            assertEquals(inputLines.get(i++), line);
        assertEquals(lines.size(), inputLines.size());
    }

    @Test
    void append() throws IOException {
        String inputLine = "line";
        fileWriter.append(inputLine);
        fileWriter.flush();
        List<String> lines = Files.readAllLines(this.path);
        int i = 0;
        for (String line : lines) {
            assertEquals(inputLine, line);
        }
        assertEquals(lines.size(), 1);
    }

    @Test
    void flush() throws IOException {
        String inputLine = "line";
        fileWriter.append(inputLine);
        fileWriter.flush();
        List<String> lines = Files.readAllLines(this.path);
        int i = 0;
        for (String line : lines)
            assertEquals(inputLine, line);
        assertEquals(lines.size(), 1);
    }
}