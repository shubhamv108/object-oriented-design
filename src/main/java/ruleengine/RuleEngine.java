package ruleengine;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RuleEngine {

    public enum Operation {
        LT, LTE, GT, GTE, EQ, EQ_IGNORE_CASE, IS_NULL, IS_NOT_NULL, IS_VALID_EPOCH_IN_MILLIS
    }

    protected static class BaseRule implements Rule {
        private final String key;
        private final Operation operation;
        private final Object value;
        private final Class<?> dataType;

        protected BaseRule(String key, Operation operation, Object value, Class<?> dataType) {
            if (key == null || key.isEmpty() || operation == null)
                throw new IllegalArgumentException();
            this.key = key;
            this.operation = operation;
            this.value = value;
            this.dataType = dataType;
        }

        public boolean evaluate(Map<String, Object> m) {
            Object val = m.get(key);
            if (Operation.IS_NULL.equals(operation))
                return val == null;
            if (val == null)
                return false;

            if (this.dataType != null && !this.dataType.isInstance(val))
                return false;

            return switch (operation) {
                case LT -> compare(val, value) < 0;
                case LTE -> compare(val, value) <= 0;
                case GT -> compare(val, value) > 0;
                case GTE -> compare(val, value) >= 0;
                case EQ -> compare(val, value) == 0;
                case EQ_IGNORE_CASE -> val instanceof String && value instanceof String
                        && ((String) val).equalsIgnoreCase((String) value);
                case IS_NULL -> val == null;
                case IS_NOT_NULL -> val != null;
                case IS_VALID_EPOCH_IN_MILLIS -> isValidEpochMillis(val);
            };
        }

        private int compare(Object val, Object target) {
            if (val instanceof Number && target instanceof Number)
                return Double.compare(((Number) val).doubleValue(), ((Number) target).doubleValue());

            if (val instanceof String && target instanceof String)
                return ((String) val).compareTo((String) target);

            throw new IllegalArgumentException();
        }

        public static boolean isValidEpochMillis(Object timestamp) {
            if (!(timestamp instanceof Number))
                return false;
            try {
                Instant value = Instant.ofEpochMilli((Long) timestamp);
                return value.isBefore(Instant.now());
            } catch (DateTimeException e) {
                return false;
            }
        }
    }

    public interface Rule {
        boolean evaluate(Map<String, Object> m);
    }

    public abstract static class AbstractRule implements Rule {
        protected final List<Rule> rules = new ArrayList<>();
        public void add(Rule rule) {
            rules.add(rule);
        }
    }

   public static class AndRule extends AbstractRule {
       public boolean evaluate(Map<String, Object> m) {
           return rules.stream().allMatch(r -> r.evaluate(m));
       }
    }

    public static class OrRule extends AbstractRule {
        public boolean evaluate(Map<String, Object> m) {
            return rules.stream().anyMatch(r -> r.evaluate(m));
        }
    }

   public static class NotRule extends AbstractRule {
        public boolean evaluate(Map<String, Object> m) {
            return rules.stream().noneMatch(r -> r.evaluate(m));
        }
    }

    public enum RuleType {
        BASE, AND, OR, NOT
    }

    public static class ConfigurableRule {
        private RuleType ruleType;
        private List<ConfigurableRule> rules;
        private String key;
        private Operation operation;
        private Object value;
        private Class<?> dataType;

        public Rule generateRule() {
            if (RuleType.BASE.equals(ruleType))
                return new BaseRule(key, operation, value, dataType);
            AbstractRule rule = switch (ruleType) {
                case BASE -> null;
                case AND -> new AndRule();
                case OR -> new OrRule();
                case NOT -> new NotRule();
            };
            if (rules == null || rules.isEmpty())
                return rule;
            rules.stream()
                    .filter(Objects::nonNull)
                    .map(ConfigurableRule::generateRule)
                    .filter(Objects::nonNull)
                    .forEach(rule::add);
            return rule;
        }
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        Map<String, Object> log = new HashMap<>();
        log.put("user_id", "101");
        log.put("type_id", "log");
        log.put("timestamp", System.currentTimeMillis());

        String config = config();
        Gson gson = new Gson();
        ConfigurableRule configurableRule = gson.fromJson(config, ConfigurableRule.class);
        Rule rule = configurableRule.generateRule();
        System.out.println(rule.evaluate(log));
    }


    private static String config() throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(RuleEngine.class.getClassLoader().getResource("config.json")).toURI());
        return Files.readString(path);
    }

}
