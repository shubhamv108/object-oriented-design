package cart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {
    String customerId;
    String name;
    List<Address> addresses = new ArrayList<>();
    String contactNumber;
    String emailId;
    ShoppingCart shoppingCart;
    List<Order> orders = new ArrayList<>();

    PromiseCart proceedToCheckout(ShoppingCart cart, Address address) {
        // couponService
        // priceService
        // taxService
        // promiseService
        return null;
    }

    boolean pay(PromiseCart cart, double finalPrice, String financialInstrument) {
        // addOrders(...);
        return true;
    }

    private void addOrders(List<Order> orders) {
        this.orders.addAll(orders);
    }
}

class Address {}

class ShoppingCart {
    List<Item> items = new ArrayList<>();

    void addItem(Item item) {}
    void removeItem(Item item) {}

}

class Item {
    String itemId;
    double price;
    boolean inStock;
    String sellerId;
    List<String> images;
}

class Seller {
    String sellerId;
    String name;
    String address;
    String gstNumber;
}

class CustomerManager {}
class SellerManager {}

class ItemManager {}

class Order {
    Item item;
    float shippingPrice;
    float packingPrice;
    float tax;

    LocalDateTime promisedDate;
    OrderStatus status;

    Address shippingAddress;

    String currentStatusDetail;
}

enum OrderStatus {
    PREPARING,
    DISPATCHED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
}

class PromiseItem {
    Item item;
    float shippingPrice;
    float packagingPrice;
    float tax;
    Date promiseDate;
}

class PromiseCart {
    List<PromiseItem> items;
    float finalPrice;

    List<Coupon> coupons = new ArrayList<>();

    void addCoupon(Coupon coupon) {}
}

class Coupon {}

 class NotificationService {}