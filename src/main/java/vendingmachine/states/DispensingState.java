package vendingmachine.states;

import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class DispensingState implements State {
    private final VendingMachine machine;

    public DispensingState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() throws MachineWarning {
        throw new MachineWarning("wait ... previous order is processing");
    }

    @Override
    public void pressButton() throws MachineWarning {
        throw new MachineWarning("wait ... previous order is processing");
    }

    @Override
    public void dispense() throws MachineWarning {
        machine.setMachineState(machine.getNoCoinInsertedState());
    }
}
