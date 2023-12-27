package cartrulengine.ruleengine;

import cartrulengine.cart.Cart;
import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.AbstractAggregateRuleChain;
import cartrulengine.commons.rulechain.AbstractRuleChain;

import java.util.Collection;
import java.util.List;

public class CartRuleChain extends AbstractAggregateRuleChain<Cart, Collection<CartItem>, Boolean> {

    public CartRuleChain(final AbstractRuleChain<Collection<CartItem>, Boolean> listCartItemRuleChain) {
        super(listCartItemRuleChain);
    }

    @Override
    protected Collection<CartItem> getAggregated(final Cart cart) {
        return cart.getCartItems();
    }

    @Override
    public Boolean getDefault(Cart object) {
        return true;
    }
}
