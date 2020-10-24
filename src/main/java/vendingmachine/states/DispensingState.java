package vendingmachine.states;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.models.Item;
import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class DispensingState implements State {
    private final VendingMachine machine;

    public DispensingState(final VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(final Coin coin) throws MachineWarning {
        throw new MachineWarning("Wait... previous order is processing");
    }

    @Override
    public void pressButton(final Button button) throws MachineWarning {
        throw new MachineWarning("Wait ... previous order is processing");
    }

    @Override
    public Item dispense() throws MachineWarning {
        Item dispensedItem = this.machine.dispense();
        this.machine.setNoCoinInsertedState();
        return dispensedItem;
    }
}
