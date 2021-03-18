package chess.exceptions;

import commons.exceptions.GameException;

public class InvalidCommandInputException extends GameException {
    public InvalidCommandInputException(final String errorMessage) {
        super(errorMessage);
    }
}
