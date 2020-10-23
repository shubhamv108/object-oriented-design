package vendingmachine.states;

import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class NoCoinInsertedState implements State {
    private final VendingMachine machine;

    public NoCoinInsertedState(final VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() throws MachineWarning {
        if (!machine.isEmpty()) machine.setMachineState(machine.getCoinInsertedState());
        else throw new MachineWarning("Cannot process request machine is out of stock");
    }

    @Override
    public void pressButton() throws MachineWarning {
        throw new MachineWarning("No coin inseted...");
    }

    @Override
    public void dispense() throws MachineWarning {
        throw new MachineWarning("Invalid operation...");
    }
}
