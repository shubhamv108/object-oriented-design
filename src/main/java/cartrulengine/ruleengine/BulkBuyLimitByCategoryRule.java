package cartrulengine.ruleengine;

import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.AbstractRuleStoreChain;
import cartrulengine.commons.rulechain.RuleStore;

import java.util.Optional;

public class BulkBuyLimitByCategoryRule extends AbstractRuleStoreChain<CartItem, Boolean, String, Integer> {

    public BulkBuyLimitByCategoryRule(final RuleStore<String, Integer> ruleStore) {
        super(ruleStore);
    }

    @Override
    public Boolean apply(final CartItem item) {
        return Optional.ofNullable(this.getValue(item.getCategory()))
                .map(limit -> item.getQuantity() <= limit && this.next(item))
                .orElse(true);
    }

    @Override
    public Boolean getDefault(CartItem object) {
        return true;
    }
}
