package commons.validators;

import commons.exceptions.GameException;

import java.util.Collection;
import java.util.Map;

public interface IValidator<OBJECT> {
    IValidator<OBJECT> validate(OBJECT object);
    IValidator<OBJECT> validateOrThrowException(OBJECT object) throws GameException;
    boolean hasMessages();
    Map<String, Collection<String>> getResult();
}