package vendingmachine.admin;

import vendingmachine.enums.Button;
import vendingmachine.IVendingMachine;
import vendingmachine.models.Item;
import vendingmachine.admin.observers.IObserver;
import vendingmachine.admin.observers.ISubscriber;

public interface IAdmin extends ISubscriber, IObserver {
    void refill(IVendingMachine machine, Button button, Item item);
    void remove(IVendingMachine machine, Button button, Item item);
    void administer(IVendingMachine machine);
}
