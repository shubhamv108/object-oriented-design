package vendingmachine;

import vendingmachine.exceptions.MachineWarning;
import vendingmachine.states.State;
import vendingmachine.admin.impl.Admin;

public interface IVendingMachine {
    boolean addAdmin(Admin admin);
    void refill(int count);
    void remove(int count);
    void insertCoin() throws MachineWarning;
    void pressButton() throws MachineWarning;
    void setMachineState(State machineState);
    boolean isEmpty();
}
