package cartrulengine.ruleengine;

import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.AbstractCollectionRuleChain;
import cartrulengine.commons.rulechain.AbstractRuleChain;

import java.util.Collection;

public class CartItemCollectionRuleChain extends AbstractCollectionRuleChain<CartItem, Boolean> {

    public CartItemCollectionRuleChain(final AbstractRuleChain<CartItem, Boolean> itemRuleChain) {
        super(itemRuleChain);
    }

    @Override
    public Boolean apply(final Collection<CartItem> cartItems) {
        for (CartItem item : cartItems)
            if (!this.applyOnElement(item))
                return false;

        return true;
    }

    @Override
    public Boolean getDefault(Collection<CartItem> object) {
        return true;
    }
}
