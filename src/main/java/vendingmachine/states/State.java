package vendingmachine.states;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.models.Item;
import vendingmachine.exceptions.MachineWarning;

public interface State {
    void insertCoin(Coin coin)throws MachineWarning;
    void pressButton(Button button)throws MachineWarning;
    Item dispense()throws MachineWarning;
}
