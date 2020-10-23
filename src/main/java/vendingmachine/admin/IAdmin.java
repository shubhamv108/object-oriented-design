package vendingmachine.admin;

import vendingmachine.impl.VendingMachine;

public interface IAdmin {
    void refill(VendingMachine machine, int count);
    void remove(VendingMachine machine, int count);
}
