package commons.inputs.reader;

import commons.input.readers.FileInputReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileInputReaderTest {

    private final String filePath = "src/test/resources/testFile";
    private FileInputReader reader;
    private final Path path = Paths.get(filePath);

    @BeforeEach
    void setUp() throws IOException {
        this.reader = FileInputReader.of(filePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (this.path.toFile().exists())
            Files.delete(this.path);
    }

    @Test
     void testIterator() throws IOException {
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(this.path, lines);
        FileInputReader fileInputReader = FileInputReader.of(filePath);
        Iterator<String> iterator = fileInputReader.iterator();
        int i = 0;
        while (iterator.hasNext())
            assertEquals(lines.get(i++), iterator.next());
        assertEquals(lines.size(), i);
    }
}