package ruleenginecomposite;

import java.util.List;

public class CompositeRuleEngine {
    public interface Rule<T> {
        boolean evaluate(T context);
    }

    public class AndRule<T> implements Rule<T> {

        private final List<Rule<T>> rules;

        public AndRule(List<Rule<T>> rules) {
            this.rules = rules;
        }

        @Override
        public boolean evaluate(T context) {
            for (Rule<T> rule : rules) {
                if (!rule.evaluate(context)) {
                    return false;
                }
            }

            return true;
        }
    }

    public class OrRule<T> implements Rule<T> {

        private final List<Rule<T>> rules;

        public OrRule(List<Rule<T>> rules) {
            this.rules = rules;
        }

        @Override
        public boolean evaluate(T context) {

            for (Rule<T> rule : rules) {
                if (rule.evaluate(context)) {
                    return true;
                }
            }

            return false;
        }
    }

    public class NotRule<T> implements Rule<T> {
        private final Rule<T> rule;

        public NotRule(Rule<T> rule) {
            this.rule = rule;
        }

        @Override
        public boolean evaluate(T context) {
            return !rule.evaluate(context);
        }
    }
}
