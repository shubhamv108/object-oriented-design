package atmmachine.states;

import atmmachine.cards.AuthenticatedCard;
import atmmachine.Cash;
import atmmachine.IAutomatedTellerMachine;

public class TransactionExecutionState implements IATMMachineState {

    private IAutomatedTellerMachine machine;

    public TransactionExecutionState(final IAutomatedTellerMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCard(AuthenticatedCard card) {

    }

    @Override
    public AuthenticatedCard removeCard() {
        return null;
    }

    @Override
    public void inputWithdrawalAmount() {

    }

    @Override
    public void inputPin() {

    }

    @Override
    public Cash dispense() {
        return null;
    }
}
