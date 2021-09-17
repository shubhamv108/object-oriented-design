package atmmachine.exceptions;

import atmmachine.CardTransactionAuthenticationStatus;

public class InvalidPINException extends ATMMachineException {
    private static final String DEFAULT_ERROR_MESSAGE = "Incorrect PIN Entered\n%s";

    private CardTransactionAuthenticationStatus cardTransactionAuthenticationStatus;

    public InvalidPINException(CardTransactionAuthenticationStatus cardTransactionAuthenticationStatus) {
        super(String.format(InvalidPINException.DEFAULT_ERROR_MESSAGE, cardTransactionAuthenticationStatus.getValue()));
    }
}
