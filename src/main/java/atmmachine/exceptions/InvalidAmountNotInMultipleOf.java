package atmmachine.exceptions;

public class InvalidAmountNotInMultipleOf extends ATMMachineException {
    public InvalidAmountNotInMultipleOf(String currency, int amount) {
        super(String.format("Entered Amount should in multiple of %s %s", currency, amount));
    }
}
