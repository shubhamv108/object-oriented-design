package ruleengine;

import ruleengine.RuleEngine.Engine;
import ruleengine.RuleEngine.EngineEntry;
import ruleengine.RuleEngine.IRule;
import ruleengine.RuleEngine.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountingSystem {

    static class User {
        private final int age;
        private final String gender;

        public User(int age, String gender) {
            this.age = age;
            this.gender = gender;
        }
    }

    enum PaymentType {
        CREDIT_CARD
    }

    static class PaymentInfo {
        private final PaymentType paymentType;
        private final String paymentProcessor;

        public PaymentInfo(final PaymentType paymentType, final String paymentProcessor) {
            this.paymentType = paymentType;
            this.paymentProcessor = paymentProcessor;
        }
    }

    static class OrderInfo {
        private final User user;
        private final String productId;
        private final PaymentInfo paymentInfo;

        public OrderInfo(User user, String productId, PaymentInfo paymentInfo) {
            this.user = user;
            this.productId = productId;
            this.paymentInfo = paymentInfo;
        }
    }

    static class DiscountCoupon {
        private final Float rate;
        private final String desc;

        public DiscountCoupon(Float rate, String desc) {
            this.rate = rate;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "DiscountCoupon{" +
                    "rate=" + rate +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    static class DiscountService {
        private final Engine engine;

        public DiscountService(final Engine engine) {
            this.engine = engine;
        }

        public List<DiscountCoupon> getApplicableCoupons(OrderInfo orderInfo) {
            Map filters = new HashMap();
            filters.put("productId", orderInfo.productId);
            filters.put("age", orderInfo.user.age);
            filters.put("gender", orderInfo.user.gender);
            filters.put("paymentType", orderInfo.paymentInfo.paymentType);
            filters.put("paymentProcessor", orderInfo.paymentInfo.paymentProcessor);

            return engine.getAllApplicable(filters)
                    .stream()
                    .map(e -> (DiscountCoupon) e)
                    .toList();
        }
    }

    public static void main(String[] args) {
        Engine engine = new RuleEngine().new Engine();

        DiscountCoupon onProduct = new DiscountCoupon(9f, "onProduct");
        IRule<String> onProductRule = new Rule<>("productId", "=", "code");
        engine.addEntry(new EngineEntry(onProductRule, onProduct));

        DiscountCoupon ageLessThan18 = new DiscountCoupon(10f, "age less than 18");
        IRule<Integer> ageLessThan18Rule = new Rule<>("age", ">=", 1).next(new Rule("age", "<=", 18));
        engine.addEntry(new EngineEntry(ageLessThan18Rule, ageLessThan18));

        DiscountCoupon onGender = new DiscountCoupon(11f, "onGender");
        IRule<String> onGenderRule = new Rule<>("gender", "=", "M");
        engine.addEntry(new EngineEntry(onGenderRule, onGender));

        DiscountCoupon onPaymentType = new DiscountCoupon(12f, "onPaymentTypeAndPaymentProcessor");
        IRule<String> onPaymentTypeRule = new Rule<>("paymentType", "=", PaymentType.CREDIT_CARD.toString()).next(new Rule("paymentProcessor", "=", "HDFC"));
        engine.addEntry(new EngineEntry(onPaymentTypeRule, onPaymentType));

        System.out.println(new DiscountService(engine).getApplicableCoupons(new OrderInfo(new User(10, "M"), "code", new PaymentInfo(PaymentType.CREDIT_CARD, "HDFC"))));
    }
}
