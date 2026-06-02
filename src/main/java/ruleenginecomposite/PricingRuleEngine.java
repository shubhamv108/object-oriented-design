package ruleenginecomposite;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PricingRuleEngine {

    interface Rule<T> {
        boolean evaluate(T context);
    }

    public class AndRule<T> implements Rule<T> {

        private final List<Rule<T>> rules;

        public AndRule(List<Rule<T>> rules) {
            this.rules = rules;
        }

        @Override
        public boolean evaluate(T context) {
            return rules.stream().allMatch(r -> r.evaluate(context));
        }
    }

    public class OrRule<T> implements CompositeRuleEngine.Rule<T> {

        private final List<Rule<T>> rules;

        public OrRule(List<Rule<T>> rules) {
            this.rules = rules;
        }

        @Override
        public boolean evaluate(T context) {
            return rules.stream().anyMatch(r -> r.evaluate(context));
        }
    }

    public class NotRule<T> implements CompositeRuleEngine.Rule<T> {
        private final Rule<T> rule;

        public NotRule(Rule<T> rule) {
            this.rule = rule;
        }

        @Override
        public boolean evaluate(T context) {
            return !rule.evaluate(context);
        }
    }

    class Customer {

        public boolean isPremium() {
            return false;
        }
    }

    class Cart {

        public double getTotal() {
            return 0;
        }

        public boolean containsCategory(String category) {
            return false;
        }

        public BigDecimal total() {
            return null;
        }
    }

    class PricingContext {
        Customer customer;
        Cart cart;
        Instant now;
        String country;
    }

    class MinOrderAmountRule implements Rule<PricingContext> {

        private final double amount;

        public MinOrderAmountRule(double amount) {
            this.amount = amount;
        }

        @Override
        public boolean evaluate(PricingContext ctx) {
            return ctx.cart.getTotal() >= amount;
        }
    }

    class PremiumCustomerRule implements Rule<PricingContext> {

        @Override
        public boolean evaluate(PricingContext ctx) {
            return ctx.customer.isPremium();
        }
    }

    class CategoryRule implements Rule<PricingContext> {

        private final String category;

        public CategoryRule(String category) {
            this.category = category;
        }

        @Override
        public boolean evaluate(PricingContext ctx) {
            return ctx.cart.containsCategory(category);
        }
    }

    interface DiscountStrategy {
        BigDecimal apply(BigDecimal originalPrice);
    }

    class PercentageDiscount implements DiscountStrategy {

        private final int percentage;

        public PercentageDiscount(int percentage) {
            this.percentage = percentage;
        }

        @Override
        public BigDecimal apply(BigDecimal amount) {
            return amount.multiply(BigDecimal.valueOf((100.0 - percentage) / 100.0));
        }
    }

    public class Promotion {
        private final String id;
        private final Rule rule;
        private final DiscountStrategy discount;
        private final int priority;
        private final boolean stackable;

        Promotion(String id, Rule rule, DiscountStrategy discount, int priority, boolean stackable) {
            this.id = id;
            this.rule = rule;
            this.discount = discount;
            this.priority = priority;
            this.stackable = stackable;
        }

        public Rule<PricingContext> getRule() {
            return null;
        }

        public DiscountStrategy getDiscount() {
            return discount;
        }
    }

    class PromotionEngine {

        public BigDecimal calculate(
                PricingContext ctx,
                List<Promotion> promotions) {

            BigDecimal total = ctx.cart.total();

            for (Promotion promotion : promotions) {

                if (promotion.getRule().evaluate(ctx)) {
                    total =
                            promotion.getDiscount()
                                    .apply(total);
                }
            }

            return total;
        }
    }
}
