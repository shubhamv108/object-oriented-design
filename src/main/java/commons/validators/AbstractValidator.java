package commons.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractValidator<OBJECT> implements IValidator<OBJECT> {

    private final Map<String, Collection<String>> messages = new LinkedHashMap<>();

    protected boolean putMessage(final String messageKey, final String messageValueFormat) {
        return this.putMessage(messageKey, messageValueFormat, null);
    }

    protected boolean putMessage(final String messageKey, final String messageValueFormat, final Object... messageValueArguments) {
        String messageValue = String.format(messageValueFormat, messageValueArguments);
        Collection<String> messagesValues = this.messages.get(messageKey);
        if (messagesValues == null) this.messages.put(messageKey, messagesValues = new ArrayList<>());
        return messagesValues.add(messageValue);
    }

    protected boolean putMessages(final String messageKey, final Collection<String> messageValue) {
        Collection<String> messagesValues = this.messages.get(messageKey);
        if (messagesValues == null) this.messages.put(messageKey, messagesValues = new ArrayList<>());
        return messagesValues.addAll(messageValue);
    }

    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }

    @Override
    public Map<String, Collection<String>> getResult() {
        Map<String, Collection<String>> copy = new LinkedHashMap<>();
        this.messages.forEach((k, v) -> copy.put(k, new ArrayList<String>(v)));
        return copy;
    }

    private Map<String, Collection<String>> getMessages() {
        return this.messages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n{\n");
        this.getMessages().forEach((k, v) -> {
            builder.append("\t").append('"').append(k).append('"').append(": ").append("[\n");
            v.forEach(e -> builder.append("\t\t").append('"').append(e).append('"').append(",\n"));
            builder.replace(builder.lastIndexOf(","), builder.lastIndexOf(",") + 1, "");
            builder.append('\t').append("]\n");
            if ("]".equals(builder.charAt(builder.length() - 1))) {
                builder.append(',');
            }
            builder.append('\n');
        });
        builder.append("}");
        return builder.toString();
    }
}
