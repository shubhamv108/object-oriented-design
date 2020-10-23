package vendingmachine.impl;

import vendingmachine.IVendingMachine;
import vendingmachine.exceptions.MachineWarning;
import vendingmachine.states.CoinInsertedState;
import vendingmachine.states.DispensingState;
import vendingmachine.states.EmptyState;
import vendingmachine.states.NoCoinInsertedState;
import vendingmachine.states.State;
import vendingmachine.admin.impl.Admin;

import java.util.LinkedHashSet;
import java.util.Set;

public class VendingMachine implements IVendingMachine {
    final State coinInsertedState = new CoinInsertedState(this);
    final State emptyState = new EmptyState(this);
    final State noCoinInsertedState = new NoCoinInsertedState(this);
    final State dispensingState = new DispensingState(this);
    State machineState;
    final Set<Admin> admins = new LinkedHashSet<>();
    int capacity = 0;

    public int getCapacity() {
        return capacity;
    }

    public VendingMachine() {
        this.machineState = getEmptyState();
    }

    @Override
    public boolean addAdmin(final Admin admin) {
        if (admin == null) throw new IllegalArgumentException("Invalid admin");
        return this.admins.add(admin);
    }

    @Override
    public void refill(final int count) {
        this.capacity += count;
        if (!isEmpty()) setMachineState(getNoCoinInsertedState());
    }

    @Override
    public void remove(final int count) {
        if (this.capacity > count) throw new IllegalArgumentException("Invalid count for removal");
        this.capacity += capacity;
    }

    @Override
    public void insertCoin() throws MachineWarning {
        this.machineState.insertCoin();
    }

    @Override
    public void pressButton() throws MachineWarning {
       this.machineState.pressButton();
       this.machineState.dispense();
       this.capacity--;
       if (isEmpty()) this.admins.forEach(admin -> admin.notify(this, new MachineWarning("Out of stock")));
    }

    @Override
    public boolean isEmpty() {
        return capacity <= 0;
    }

    public State getCoinInsertedState() {
        return coinInsertedState;
    }

    public State getEmptyState() {
        return emptyState;
    }

    public State getNoCoinInsertedState() {
        return noCoinInsertedState;
    }

    public State getDispensingState() {
        return dispensingState;
    }

    @Override
    public void setMachineState(final State machineState) {
        this.machineState = machineState;
    }
}
