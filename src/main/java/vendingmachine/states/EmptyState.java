package vendingmachine.states;

import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class EmptyState implements State {
    private final VendingMachine machine;

    public EmptyState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() throws MachineWarning {

    }

    @Override
    public void pressButton() throws MachineWarning {

    }

    @Override
    public void dispense() throws MachineWarning {

    }
}
