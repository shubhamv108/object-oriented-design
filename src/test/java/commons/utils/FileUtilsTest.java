package commons.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilsTest {

    private FileUtils utils;
    private final String filePath = "src/test/resources/testFile";
    private final Path path = Paths.get(filePath);

    @BeforeEach
    void setUp() {
        this.utils = FileUtils.getInstance();
    }

    @AfterEach
    void tearDown() throws IOException {
        this.utils = null;
        if (path.toFile().exists())
            Files.delete(Path.of(filePath));
    }

    @Test
    void testGetInstance() {
        assertNotNull(this.utils);
    }

    @Test
    void testReadAndGetLines() throws IOException {
        String filePath = "src/test/resources/input.txt";
        List<String> readLines = this.utils.readAndGetLines(filePath);
        List<String> lines = Files.readAllLines(Path.of(filePath));
        assertEquals(readLines.size(), lines.size());
        for (int i = 0; i < readLines.size(); i++)
            assertEquals(readLines.get(i), lines.get(i));
    }

    @Test
    void testAppend() throws IOException {
        String line = "Line1";
        Files.createFile(this.path);
        this.utils.append(filePath, line);
        List<String> lines = Files.readAllLines(this.path);
        assertEquals(1, lines.size());
        assertEquals(line, lines.get(0));
    }

    @Test
    void testCreateFile() throws IOException {
        String filePath = "src/test/resources/testFile";
        this.utils.createFile(filePath);
        Path path = Paths.get(filePath);
        assertTrue(path.toFile().exists());
    }

    @Test
    void testCreateFileIfNotExists() throws IOException {
        Files.createFile(path);
        this.utils.createFileIfNotExists(path);
        assertTrue(path.toFile().exists());
        Files.delete(path);
        this.utils.createFileIfNotExists(path);
        assertTrue(path.toFile().exists());
    }

    @Test
    void testGetAppendWriter() throws IOException {
        Files.createFile(this.path);
        Writer writer = this.utils.getAppendWriter(this.path);
        List<String> lines = Arrays.asList("Line1", "Line");
        writer.append(lines.get(0) + "\n");
        writer.append(lines.get(1) + "\n");
        writer.flush();
        List<String> readLines = Files.readAllLines(this.path);
        assertEquals(readLines.size(), lines.size());
        for (int i = 0; i < readLines.size(); i++)
            assertEquals(readLines.get(i), lines.get(i));
    }
}