package atmmachine.exceptions;

public class NotEnoughCashToDispense extends ATMMachineException {
    public NotEnoughCashToDispense() {
        super("Not enough money to dispense");
    }
}
