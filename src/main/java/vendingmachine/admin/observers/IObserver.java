package vendingmachine.admin.observers;

import vendingmachine.impl.VendingMachine;

public interface IObserver extends ISubscriber {
    int getCapacity(VendingMachine machine);
}
