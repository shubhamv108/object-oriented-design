package commons.writers;

import commons.utils.FileUtils;

import java.io.IOException;
import java.io.Writer;

public class FileWriter implements IWriter {

    private final String filePath;
    private final FileUtils fileUtils;
    private final Writer writer;

    private FileWriter(final String filePath) throws IOException {
        this(filePath, FileUtils.getInstance());
    }

    public static FileWriter of(final String filePath) throws IOException {
        return new FileWriter(filePath);
    }

    public FileWriter(final String filePath, final FileUtils fileUtils) throws IOException {
        this.filePath = filePath;
        this.fileUtils = fileUtils;
        this.fileUtils.createFile(filePath);
        this.writer = this.fileUtils.getAppendWriter(filePath);
        this.attachShutdownHook();
    }

    @Override
    public void append(final String line) throws IOException {
        this.writer.write(line + "\n");
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    private void attachShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.flush();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }));
    }

}
