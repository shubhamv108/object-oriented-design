package amazon.actors;

import amazon.cart.Cart;
import amazon.search.ISearchService;
import vendingmachine.models.Item;

public abstract class Customer {

    Cart cart;
    ISearchService searchService;

    public boolean addItem(Item item) {}
    public boolean updateItem(Item item) {}
    public boolean deleteItem(Item item) {}

    public Cart getCart() {
        return cart;
    }
}
