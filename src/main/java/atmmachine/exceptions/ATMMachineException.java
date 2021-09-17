package atmmachine.exceptions;

public abstract class ATMMachineException extends RuntimeException {

    protected ATMMachineException(final String errorMessage) {
        super(errorMessage);
    }

}
