package atmmachine.states;

import atmmachine.cards.AuthenticatedCard;
import atmmachine.Cash;

public class CardInsertedState implements IATMMachineState {
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
