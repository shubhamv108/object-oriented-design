package vendingmachine.states;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.models.Item;
import vendingmachine.exceptions.MachineWarning;
import vendingmachine.impl.VendingMachine;

public class EmptyState implements State {
    private final VendingMachine machine;

    public EmptyState(final VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(final Coin coin) throws MachineWarning {
        throw new MachineWarning("Empty Machine");
    }

    @Override
    public void pressButton(final Button button) throws MachineWarning {
        throw new MachineWarning("Empty Machine");
    }

    @Override
    public Item dispense() throws MachineWarning {
        throw new MachineWarning("Empty Machine");
    }
}
