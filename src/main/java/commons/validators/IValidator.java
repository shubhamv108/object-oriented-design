package commons.validators;

import commons.exceptions.GameException;

import java.util.Collection;
import java.util.Map;

public interface IValidator<OBJECT> {

    String IS_EMPTY = "%s is empty.";
    String MUST_NOT_BE_EMPTY = "%s must not be empty.";

    IValidator<OBJECT> validate(OBJECT object);
    IValidator<OBJECT> validateOrThrowException(OBJECT object);
    boolean hasMessages();
    Map<String, Collection<String>> getResult();
}