package vendingmachine.admin.impl;

import vendingmachine.impl.VendingMachine;
import vendingmachine.admin.IAdmin;
import vendingmachine.exceptions.MachineWarning;
import vendingmachine.admin.observers.IObserver;

import java.util.HashSet;
import java.util.Set;

public class Admin implements IAdmin, IObserver {

    private final Set<VendingMachine> machines = new HashSet<>();

    public void addMachine(final VendingMachine machine) {
        this.machines.add(machine);
    }

    @Override
    public void refill(final VendingMachine machine, final int count) {
        this.getMachine(machine).refill(count);
    }

    @Override
    public void remove(final VendingMachine machine, final int count) {
        this.getMachine(machine).remove(count);
    }

    @Override
    public boolean notify(VendingMachine machine, MachineWarning notification) {
        System.out.println(machine + ": " + notification.getErrorMsg());
        return true;
    }

    @Override
    public boolean subscribe(VendingMachine machine) {
        if (machine.addAdmin(this)) {
            return this.machines.add(machine);
        }
        return false;
    }

    @Override
    public int getCapacity(VendingMachine machine) {
        return this.getMachine(machine).getCapacity();
    }

    private VendingMachine getMachine(VendingMachine machine) {
        return this.machines.stream().filter(machine::equals).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not admin"));
    }
}
