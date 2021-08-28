package commons.exceptions;

public class InvalidCommandException extends ClientException {
    public InvalidCommandException(final Object command, final String... message) {
        super(String.format("Invalid command %s, %s", command, message));
    }
}
