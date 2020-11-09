package commons.validators;

import commons.validators.IValidator;
import java.util.List;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractValidator<Data> implements IValidator<Data> {

    protected Map<String, List<String>> messages = new LinkedHashMap<>();

    protected boolean putMessage(final String messageKey, final String messageValue) {
        List<String> messagesValues = this.messages.get(messageKey);
        if (messagesValues == null) this.messages.put(messageKey, messagesValues = new ArrayList<>());
        return messagesValues.add(messageValue);
    }

    protected boolean putMessages(final String messageKey, final List<String> messageValue) {
        List<String> messagesValues = this.messages.get(messageKey);
        if (messagesValues == null) this.messages.put(messageKey, messagesValues = new ArrayList<>());
        return messagesValues.addAll(messageValue);
    }

    public Map<String, List<String>> getMessages() {
        Map<String, List<String>> copy = new LinkedHashMap<>();
        this.messages.forEach((k, v) -> copy.put(k, new ArrayList<String>(v)));
        return copy;
    }

}
