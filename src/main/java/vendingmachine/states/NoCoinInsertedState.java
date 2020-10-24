package vendingmachine.states;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.models.Item;
import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class NoCoinInsertedState implements State {
    private final VendingMachine machine;

    public NoCoinInsertedState(final VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(final Coin coin) throws MachineWarning {
        if (this.machine.isEmpty()) throw new MachineWarning("Cannot process request machine is out of stock");
        this.machine.setCoinInsertedState(coin);
    }

    @Override
    public void pressButton(final Button button) throws MachineWarning {
        throw new MachineWarning("No coin inserted...");
    }

    @Override
    public Item dispense() throws MachineWarning {
        throw new MachineWarning("Invalid operation...");
    }
}
