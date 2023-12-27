package cartrulengine;

import cartrulengine.cart.Cart;
import cartrulengine.cart.CartItem;
import cartrulengine.commons.rulechain.RuleStore;

public class Main {

    public static void main(String[] args) {
        final Cart cart = setupAdGetCart();

        final int maxBulkBuyLimit = 10;
        final RuleStore<String, Integer> categoryRuleStore = setupAdGetCategoryRuleStore();
        final var cartRuleEngine = CartRuleEngineFactory.generate(maxBulkBuyLimit, categoryRuleStore);

        System.out.println(
            cartRuleEngine.apply(cart) ? "MET" : "BREACHED"
        );

    }

    public static Cart setupAdGetCart() {
        final Cart cart = new Cart();
        cart.put(new CartItem("1", "Paracetamol", 3));
        cart.put(new CartItem("2", "analgesic", 3));
        cart.put(new CartItem("1", "Paracetamol", 5));
        cart.put(new CartItem("3", "chocolate", 10));
        cart.put(new CartItem("4", " Paracetamol", 2));
        return cart;
    }



    public static RuleStore<String, Integer> setupAdGetCategoryRuleStore() {
        final RuleStore<String, Integer> ruleStore = new RuleStore<>();
        ruleStore.put("Paracetamol", 5);
        return ruleStore;
    }

}
