package vendingmachine.impl;

import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.IVendingMachine;
import vendingmachine.models.Item;
import vendingmachine.admin.impl.Admin;
import vendingmachine.admin.observers.ISubscriber;
import vendingmachine.exceptions.MachineWarning;
import vendingmachine.states.CoinInsertedState;
import vendingmachine.states.DispensingState;
import vendingmachine.states.EmptyState;
import vendingmachine.states.NoCoinInsertedState;
import vendingmachine.states.State;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class VendingMachine implements IVendingMachine {
    private final State coinInsertedState = new CoinInsertedState(this);
    private final State emptyState = new EmptyState(this);
    private final State noCoinInsertedState = new NoCoinInsertedState(this);
    private final State dispensingState = new DispensingState(this);
    private State machineState;
    private final Set<Admin> admins = new LinkedHashSet<>();
    private final Set<ISubscriber> subscribedAdmins = new LinkedHashSet<>();
    private Map<Button, Set<Item>> items = new LinkedHashMap<>();
    private Coin insertedCoin;
    private Button pressedButton;
    private int maxItemNamesCount;
    private int maxItemsPerName;
    private int capacity;

    public VendingMachine(final int maxItemNamesCount, final int maxItemsPerName) {
        this.maxItemNamesCount = maxItemNamesCount;
        this.maxItemsPerName = maxItemsPerName;
        this.machineState = this.getEmptyState();
    }

    @Override
    public boolean addAdmin(final Admin admin) {
        if (admin == null) throw new IllegalArgumentException("Null admin");
        if (this.admins.contains(admin)) throw new MachineWarning("Already Admin");
        this.attach(admin);
        return this.admins.add(admin);
    }

    @Override
    public void refill(final Button button, final Item item) {
        if (item == null) throw new IllegalArgumentException("Null item");
        Set<Item> items = this.getItemsByNameOrThrowException(item);
        if (items == null) this.items.put(button, items = new LinkedHashSet<>());
        items.add(item);
        this.incCapacity();
        this.setMachineState(this.getNoCoinInsertedState());
    }

    private Set<Item> getItemsByNameOrThrowException(final Item item) {
        Set<Item> items = this.items.get(item.getName());
        if (items == null) {
            if (this.items.size() == this.maxItemNamesCount) throw new MachineWarning("Maximum item type capacity");
        } else if (items.size() == this.maxItemsPerName) {
            throw new MachineWarning("Max capacity for " + item + " reached");
        }
        return items;
    }

    @Override
    public Item remove(final Button button, final Item item) {
        Item removedItem;
        Set<Item> items = this.items.get(button);
        if (items == null || items.size() == 0) throw new MachineWarning("No items with given button present");
        removedItem = items.stream().filter(i -> i == item).findFirst().orElse(items.stream().findFirst().get());
        items.remove(removedItem);
        this.decCapacity();
        return removedItem;
    }

    @Override
    public void insertCoin(final Coin coin) throws MachineWarning {
        this.machineState.insertCoin(coin);
    }

    @Override
    public Item pressButton(final Button button) throws MachineWarning {
        this.machineState.pressButton(button);
        Item item = this.machineState.dispense();
        if (isEmpty(button))
            this.subscribedAdmins.forEach(admin -> admin.notify(this, new MachineWarning("Out of stock for button " + button)));
        return item;
    }

    @Override
    public boolean isEmpty() {
        return this.capacity == 0;
    }

    private boolean isEmpty(final Button button) {
        Set<Item> items = this.items.get(button);
        return items == null || items.size() == 0;
    }

    public void setCoinInsertedState(final Coin coin) {
        this.setInsertedCoin(coin);
        this.setMachineState(this.getCoinInsertedState());
    }

    @Override
    public Item dispense() {
        Set<Item> items = this.items.get(this.getPressedButton());
        Item dispensedItem = this.items.get(this.getPressedButton()).stream().findFirst().get();
        items.remove(dispensedItem);
        this.decCapacity();
        return dispensedItem;
    }

    private Button getPressedButton() {
        return this.pressedButton;
    }

    @Override
    public boolean isButtonActive(final Button button) throws MachineWarning {
        return this.items.containsKey(button);
    }

    public State getCoinInsertedState() {
        return this.coinInsertedState;
    }

    public State getEmptyState() {
        return this.emptyState;
    }

    public State getNoCoinInsertedState() {
        return this.noCoinInsertedState;
    }

    public State getDispensingState() {
        return this.dispensingState;
    }

    public void setDispensingState(final Button button) {
        this.setPressedButton(button);
        this.setMachineState(this.getDispensingState());
    }

    public void setNoCoinInsertedState() {
        this.setInsertedCoin(null);
        this.setPressedButton(null);
        this.setMachineState(this.getNoCoinInsertedState());
    }

    public void setMachineState(final State machineState) {
        this.machineState = machineState;
    }

    private void setInsertedCoin(final Coin insertedCoin) {
        this.insertedCoin = insertedCoin;
    }

    private void setPressedButton(final Button button) {
        this.pressedButton = button;
    }

    @Override
    public boolean attach(ISubscriber subscriber) {
        if (this.subscribedAdmins.contains(subscriber)) throw new MachineWarning("Admin already subscribed");
        return this.subscribedAdmins.add(subscriber);
    }

    @Override
    public boolean detach(ISubscriber subscriber) {
        return this.subscribedAdmins.remove(subscriber);
    }

    @Override
    public Object getState() {
        Map<Button, List<Item>> copy = new LinkedHashMap<>();
        this.items.forEach((k, v) ->  copy.put(k, new ArrayList<Item>(v)));
        return copy;
    }

    private void incCapacity() {
        this.capacity++;
    }

    private void decCapacity() {
        this.capacity--;
    }
}
