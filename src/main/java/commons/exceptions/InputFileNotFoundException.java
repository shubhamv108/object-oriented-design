package commons.exceptions;

public class InputFileNotFoundException extends RuntimeException {
    public InputFileNotFoundException(final String filePath) {
        super(String.format("Input file %s, Not found", filePath));
    }
}
