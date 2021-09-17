package atmmachine.states;

import atmmachine.cards.AuthenticatedCard;
import atmmachine.Cash;

public interface IATMMachineState {
    void insertCard(AuthenticatedCard card);
    AuthenticatedCard removeCard();
    void inputWithdrawalAmount();
    void inputPin();
    Cash dispense();
}
