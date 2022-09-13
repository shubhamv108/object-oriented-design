package amazon.cart;

import vendingmachine.models.Item;

import java.util.List;

public class Cart {
    List<Item> items;
    double cartValue;

    public boolean addItem(Item item) { return false; }
    public boolean updateItem(Item item) { return false; }
    public boolean deletetem(Item item) { return false; }
    public void checkout() {}

    public List<Item> getItems() {
        return items;
    }

    public double getCartValue() {
        return cartValue;
    }
}
