package atmmachine.exceptions;

public class ErrorReadingCard extends ATMMachineException {
    protected ErrorReadingCard(final String errorMessage) {
        super(errorMessage);
    }
}
