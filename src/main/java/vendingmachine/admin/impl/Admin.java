package vendingmachine.admin.impl;

import vendingmachine.enums.Button;
import vendingmachine.IVendingMachine;
import vendingmachine.models.Item;
import vendingmachine.admin.IAdmin;
import vendingmachine.admin.observers.INotification;
import vendingmachine.admin.observers.IObservable;
import vendingmachine.admin.observers.ISubscription;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Admin implements IAdmin {

    private final String id;

    public String getId() {
        return id;
    }

    private final Set<IVendingMachine> machines = new HashSet<>();

    public Admin(final String id) {
        this.id = id;
    }

    @Override
    public void refill(final IVendingMachine machine, final Button button, final Item item) {
        this.getMachine(machine).refill(button, item);
    }

    @Override
    public void remove(final IVendingMachine machine, final Button button, final Item item) {
        this.getMachine(machine).remove(button, item);
    }

    @Override
    public void administer(final IVendingMachine machine) {
        if (machine.addAdmin(this)) {
            this.addMachine(machine);
        }
    }

    private boolean addMachine(final IVendingMachine machine) {
        return this.machines.add(machine);
    }

    @Override
    public boolean notify(final ISubscription subscription, final INotification notification) {
        System.out.println(subscription + ": " + notification.get());
        return true;
    }

    @Override
    public boolean subscribe(final ISubscription subscription) {
        return subscription.attach(this);
    }

    @Override
    public boolean unsubscribe(final ISubscription subscription) {
        return subscription.detach(this);
    }

    @Override
    public Object pullState(final IObservable observable) {
        return this.getMachine((IVendingMachine) observable).getState();
    }


    private IVendingMachine getMachine(final IVendingMachine machine) {
        return this.machines.stream().filter(machine::equals).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not admin"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return id.equals(admin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
