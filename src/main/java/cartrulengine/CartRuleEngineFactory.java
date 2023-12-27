package cartrulengine;

import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.AbstractRuleChain;
import cartrulengine.commons.rulechain.RuleStore;
import cartrulengine.ruleengine.BulkBuyLimitByCategoryRule;
import cartrulengine.ruleengine.BulkBuyLimitRule;
import cartrulengine.ruleengine.CartItemCollectionRuleChain;
import cartrulengine.ruleengine.CartRuleChain;

public class CartRuleEngineFactory {

    public static CartRuleChain generate(
            final int maxBulkBuyLimit,
            final RuleStore<String, Integer> categoryRuleStore) {

        final AbstractRuleChain<CartItem, Boolean> cartItemRuleChain =
                new BulkBuyLimitRule(maxBulkBuyLimit).setNext(new BulkBuyLimitByCategoryRule(categoryRuleStore));

        final CartItemCollectionRuleChain cartItemCollectionRuleChain =
                new CartItemCollectionRuleChain(cartItemRuleChain);

        return new CartRuleChain(cartItemCollectionRuleChain);
    }

}
