package loggerlibrary.sinks.implementations;

import commons.builder.IBuilder;
import loggerlibrary.api.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileSink extends AbstractSink {

    private final Path filePath;
    private final File file;

    private FileSink(final int id, final String filePath) {
        super(id);
        this.filePath = Paths.get(filePath);
        this.file = Paths.get(filePath).toFile();
        this.createFile();
    }

    private synchronized boolean createFile() {
        try {
            Files.createDirectories(this.filePath.getParent());
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.file.exists();

    }

    @Override
    public void writeMessage(final Message message) {
        try {
            Files.write(this.filePath, message.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (final IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public static FileSinkBuilder builder() {
        return new FileSinkBuilder();
    }

    public static class FileSinkBuilder implements IBuilder<FileSink> {
        private int id;
        private String filePath;

        public FileSinkBuilder withId(final int id) {
            this.id = id;
            return this;
        }

        public FileSinkBuilder withFilePath(final String filePath) {
            this.filePath = filePath;
            return this;
        }

        @Override
        public FileSink build() {
            return new FileSink(this.id, this.filePath);
        }
    }
}
