package amazon.actors;

import amazon.Review;
import amazon.cart.Cart;
import shipmenttracking.models.Order;

import java.util.List;

public class Buyer extends User {

    List<Order> orders;

    public void addReview(Review review) {}
    public Order placeOrder(Cart cart) { return null; }

}
