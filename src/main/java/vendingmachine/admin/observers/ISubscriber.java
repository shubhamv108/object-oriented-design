package vendingmachine.admin.observers;

import vendingmachine.exceptions.MachineWarning;
import vendingmachine.impl.VendingMachine;

public interface ISubscriber {
    boolean notify(VendingMachine machine, MachineWarning notification);
    boolean subscribe(VendingMachine machine);
}
