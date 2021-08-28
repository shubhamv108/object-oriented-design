package commons.readers.input;

import commons.exceptions.InputFileNotFoundException;
import commons.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class FileInputReader extends AbstractInputReader {

    private final String filePath;
    private final FileUtils fileUtils;

    private FileInputReader(final String filePath) throws FileNotFoundException {
        this(filePath, FileUtils.getInstance());
    }

    private FileInputReader(final String filePath, final FileUtils fileUtils) throws FileNotFoundException {
        this(fileUtils.getBufferedReader(filePath), filePath, fileUtils);
    }

    private FileInputReader(final BufferedReader bufferedReader, final String filePath, final FileUtils fileUtils) {
        super(bufferedReader);
        this.filePath = filePath;
        this.fileUtils = fileUtils;
    }

    public static FileInputReader of(final String filePath) {
        try {
            return new FileInputReader(filePath);
        } catch (final FileNotFoundException fnfe) {
            throw new InputFileNotFoundException(filePath);
        }
    }

    private List<String> getAllLines() {
        try {
            return this.fileUtils.readAndGetLines(this.filePath);
        } catch (final FileNotFoundException fnfe) {
            throw new InputFileNotFoundException(this.filePath);
        } catch (final NoSuchFileException nsfe) {
            throw new InputFileNotFoundException(this.filePath);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
