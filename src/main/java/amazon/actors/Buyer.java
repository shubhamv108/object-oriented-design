package amazon.actors;

import shipmenttracking.models.Order;

import java.util.List;

public class Buyer extends User {

    List<Order> orders;

    public void addReview(Review review) {}
    public Order placeOrder(Cart cart) {}

}
