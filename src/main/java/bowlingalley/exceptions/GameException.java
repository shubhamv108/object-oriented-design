package bowlingalley.exceptions;

public class GameException extends RuntimeException {

    public GameException(final String errorMessage) {
        super(errorMessage);
    }

}
