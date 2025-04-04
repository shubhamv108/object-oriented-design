package ruleengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleEngine {

    public interface IRule<V> {
        boolean testAny(Map<String, V> keyValuePairs);
        boolean test(Map<String, V> keyValuePairs);
        boolean test(String key, V value);

        IRule<V> next();
        IRule<V> next(IRule next);
    }

    public static abstract class AbstractRule<V> implements IRule<V> {
        private IRule<V> next;

        @Override
        public IRule<V> next(IRule next) {
            return this.next = next;
        }

        @Override
        public IRule<V> next() {
            return this.next;
        }


        @Override
        public boolean testAny(Map<String, V> keyValuePairs) {
            return keyValuePairs
                    .entrySet()
                    .stream()
                    .anyMatch(e -> test(e.getKey(), e.getValue()));
        }

        @Override
        public boolean test(Map<String, V> keyValuePairs) {
            return keyValuePairs
                    .entrySet()
                    .stream()
                    .allMatch(e -> test(e.getKey(), e.getValue()));
        }

        public boolean test(String key, V value) {
            return next.test(key, value);
        }
    }

    public static class Rule<V extends Comparable> extends AbstractRule<V> {
        private final String key;
        private final String operator;
        private final V value;

        public Rule(final String key, final String operator, final V value) {
            this.key = key;
            this.operator = operator;
            this.value = value;
        }

        @Override
        public boolean test(String key, V value) {
            if (!this.key.equals(key))
                return false;

            if (this.value == null)
                return value == null;

            if (value == null)
                return false;

            if (value instanceof Map<?, ?>)
                return super.test((Map<String, V>) value);

            switch (operator) {
                case "=":  return this.value.equals(value);
                case "<":  return this.value.compareTo(value) > 0;
                case ">":  return this.value.compareTo(value) < 0;
                case "<=": return this.value.compareTo(value) >= 0;
                case ">=": return this.value.compareTo(value) <= 0;
                case "!=": return this.value.compareTo(value) != 0;
                default: return this.next().test(key, value);
            }
        }
    }

    public static class CollectionRule<V extends Comparable> extends AbstractRule<V>  {
        private final String key;
        private final String operator;
        private final Collection<V> value;

        public CollectionRule(String key, String operator, Collection<V> value) {
            this.key = key;
            this.operator = operator;
            this.value = value;
        }

        public boolean test(String key, V value) {
            if (!this.key.equals(key))
                return false;

            if (this.value == null)
                return value == null;

            if (value == null)
                return false;

            if (value instanceof Collection<?>)
                return test(key, (Collection<V>) value);

            switch (operator) {
                case "has":  return this.value.contains(value);
                case "!has": return !this.value.contains(value);
                default: return this.next().test(key, value);
            }
        }

        public boolean test(String key, Collection<V> value) {
            if (!this.key.equals(key))
                return false;

            if (this.value == null)
                return value == null;

            if (value == null)
                return false;

            switch (operator) {
                case "=":  return this.value.equals(value);
                case "hasAll": return !this.value.containsAll(value);
                case "hasAny": return value.stream().anyMatch(this.value::contains);
                case "hasNone": return value.stream().noneMatch(this.value::contains);
                default: return false;
            }
        }
    }

    static class EngineEntry {
        private final IRule<?> rule;
        private final Object value;

        public EngineEntry(IRule<?> rule, Object value) {
            this.rule = rule;
            this.value = value;
        }
    }

    class Engine {
        private final List<EngineEntry> entries = new ArrayList<>();

        public void addEntry(EngineEntry engineEntry) {
            this.entries.add(engineEntry);
        }

        public List<Object> getAllApplicable(Map m) {
            return entries
                    .stream()
                    .filter(engineEntry -> engineEntry.rule.testAny(m))
                    .map(engineEntry -> engineEntry.value)
                    .toList();
        }
    }

    public static void main(String[] args) {
        IRule<Integer> ageRule = new Rule<>("age", ">=", 1).next(new Rule("age", "<=", 18));

        Engine engine = new RuleEngine().new Engine();
        engine.addEntry(new EngineEntry(ageRule, "test"));
        System.out.println(engine.getAllApplicable(new HashMap<>() {{put("age", 1);}}));
    }

}
