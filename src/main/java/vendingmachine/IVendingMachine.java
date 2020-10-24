package vendingmachine;

import vendingmachine.admin.impl.Admin;
import vendingmachine.admin.observers.IObservable;
import vendingmachine.admin.observers.ISubscription;
import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.exceptions.MachineWarning;
import vendingmachine.models.Item;
import vendingmachine.states.State;

public interface IVendingMachine extends ISubscription, IObservable {
    boolean addAdmin(Admin admin) throws MachineWarning;
    void refill(Button button, Item item) throws MachineWarning;
    Item remove(Button button, Item item) throws MachineWarning;
    void insertCoin(Coin coin) throws MachineWarning;
    Item pressButton(Button button) throws MachineWarning;

    boolean isButtonActive(Button button) throws MachineWarning;

    void setDispensingState(Button button);

    void setNoCoinInsertedState() throws MachineWarning;
    void setMachineState(State machineState) throws MachineWarning;

    boolean isEmpty();

    void setCoinInsertedState(Coin coin) throws MachineWarning;

    Item dispense();
}
