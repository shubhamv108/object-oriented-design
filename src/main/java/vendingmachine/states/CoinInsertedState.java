package vendingmachine.states;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.models.Item;
import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class CoinInsertedState implements State {
    private final VendingMachine machine;

    public CoinInsertedState(final VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(final Coin coin) throws MachineWarning {
        throw new MachineWarning("Coin already inserted");
    }

    @Override
    public void pressButton(final Button button) throws MachineWarning {
        if (!this.machine.isButtonActive(button)) throw new MachineWarning("Button inactive. Press any other Button");
        this.machine.setDispensingState(button);
    }

    @Override
    public Item dispense() throws MachineWarning {
        throw new MachineWarning("Dispense button is not pressed");
    }
}
