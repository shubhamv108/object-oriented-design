package commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileUtils {

    private FileUtils() {}

    private static class SingletonHolder {
        private static final FileUtils INSTANCE = new FileUtils();
    }

    public static FileUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<String> readAndGetLines(final String filePath) throws IOException {
        return readAndGetLines(Paths.get(filePath));
    }

    public List<String> readAndGetLines(final Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }

    public Path append(final String filePath, final List<String> lines) throws IOException {
        return Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

    public Path append(final String filePath, final String line) throws IOException {
        return Files.write(Paths.get(filePath), line.getBytes(), StandardOpenOption.APPEND);
    }

    public Path createFile(final String filePath) throws IOException {
        File file = Paths.get(filePath).toFile();
        if (Paths.get(filePath).toFile().exists()) {
            file.delete();
        }
        return this.createFileIfNotExists(Paths.get(filePath));
    }

    public Path createFileIfNotExists(final Path filePath) throws IOException {
        if (!filePath.toFile().exists()) {
            Files.createDirectories(filePath.toAbsolutePath().getParent());
            Files.createFile(filePath);
        }
        return filePath;
    }

    public Writer getAppendWriter(final String filePath) throws IOException {
        return this.getAppendWriter(Paths.get(filePath));
    }
    public Writer getAppendWriter(final Path filePath) throws IOException {
        return Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

}
