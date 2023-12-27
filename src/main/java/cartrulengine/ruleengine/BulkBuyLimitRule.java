package cartrulengine.ruleengine;

import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.AbstractRuleChain;

public class BulkBuyLimitRule extends AbstractRuleChain<CartItem, Boolean> {

    private final int maxQuantity;

    public BulkBuyLimitRule(final int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @Override
    public Boolean apply(final CartItem item) {
        return item.getQuantity() > this.maxQuantity ? false : this.next(item);
    }

    @Override
    public Boolean getDefault(CartItem object) {
        return true;
    }
}
