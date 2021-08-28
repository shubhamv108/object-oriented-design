package commons.validators.exceptions;

import java.util.Collection;
import java.util.Map;

public class InvalidParametersException extends RuntimeException {

    private final StringBuilder errorMessageBuilder = new StringBuilder();

    public InvalidParametersException(final Map<String, Collection<String>> messages) {
        this.addAll(messages);
    }

    private void addAll(final Map<String, Collection<String>> messages) {
        if (messages != null)
            for (Map.Entry<String, Collection<String>> entry : messages.entrySet()) {
                String errorMessage = entry.getKey();
                Collection<String> params = entry.getValue();
                this.errorMessageBuilder
                        .append(String.format(String.join(", ", params), errorMessage));
            }
    }

}
