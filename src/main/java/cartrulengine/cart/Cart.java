package cartrulengine.cart;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Cart {

    private final Map<String, CartItem> cartItems = new LinkedHashMap<>();

    public Stream<CartItem> stream() {
        return cartItems.values().stream();
    }

    public void put(final CartItem cartItem) {
        final CartItem item = this.cartItems.computeIfAbsent(
                cartItem.getProductId(),
                e -> new CartItem());
        item.setProductId(cartItem.getProductId());
        item.setCategory(cartItem.getCategory());
        item.setQuantity(cartItem.getQuantity());
    }

    public Collection<CartItem> getCartItems() {
        return cartItems.values();
    }
}
