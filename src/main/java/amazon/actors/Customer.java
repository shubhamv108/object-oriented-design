package amazon.actors;

import amazon.cart.Cart;
import amazon.search.ISearchService;
import vendingmachine.models.Item;

public abstract class Customer {

    Cart cart;
    ISearchService searchService;

    public boolean addItem(Item item) { return false; }
    public boolean updateItem(Item item) { return false; }
    public boolean deleteItem(Item item) { return false; }

    public Cart getCart() {
        return cart;
    }
}
