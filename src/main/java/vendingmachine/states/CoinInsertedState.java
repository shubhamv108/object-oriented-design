package vendingmachine.states;

import vendingmachine.impl.VendingMachine;
import vendingmachine.exceptions.MachineWarning;

public class CoinInsertedState implements State {
    private final VendingMachine machine;

    public CoinInsertedState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() throws MachineWarning {
        throw new MachineWarning("Coin already inserted");
    }

    @Override
    public void pressButton() throws MachineWarning {
        throw new MachineWarning("Dispense button is not pressed");
    }

    @Override
    public void dispense() throws MachineWarning {
        machine.setMachineState(machine.getDispensingState());
    }
}
