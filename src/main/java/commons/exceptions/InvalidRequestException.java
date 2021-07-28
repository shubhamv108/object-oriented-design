package commons.exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InvalidRequestException extends ClientException {

    private Map<String, Collection<String>> errorMessages;

    public InvalidRequestException(final Map<String, Collection<String>> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public InvalidRequestException putErrorMessage(final String errorKey, final String errorMessage) {
        Collection<String> errorMessages = this.errorMessages.get(errorKey);
        if (Objects.isNull(errorMessages)) {
            errorMessages = new ArrayList<>();
            this.errorMessages.put(errorKey, errorMessages);
        }
        errorMessages.add(errorMessage);
        return get();
    }

    public InvalidRequestException putErrorMessages(final Map<String, Collection<String>> errorMessages) {
        if (this.errorMessages == null) {
            this.errorMessages = errorMessages;
        } else {
            errorMessages.forEach((k, v) -> v.forEach(errorMessage -> this.putErrorMessage(k, errorMessage)));
        }
        return get();
    }

    private InvalidRequestException get() {
        return this;
    }

    private Map<String, Collection<String>> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n{\n");
        this.getErrorMessages().forEach((k, v) -> {
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