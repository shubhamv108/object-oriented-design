package atmmachine.exceptions;

public class CardBlockedForTransaction extends ATMMachineException {
    private static final String errorMessage = "AuthenticatedCard blocked for transaction\nReason: Incorrect PIN entered 3 times";

    public CardBlockedForTransaction() {
        super("AuthenticatedCard blocked for transaction");
    }
}
