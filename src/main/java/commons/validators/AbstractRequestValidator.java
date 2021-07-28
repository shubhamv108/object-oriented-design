package commons.validators;

import commons.exceptions.GameException;
import commons.exceptions.InvalidRequestException;

public class AbstractRequestValidator<Request> extends AbstractValidator<Request> {
    @Override
    public IValidator<Request> validate(Request request) {
        return this;
    }

    @Override
    public IValidator<Request> validateOrThrowException(final Request request) throws GameException {
        this.validate(request);
        if (this.hasMessages()) {
            throw new InvalidRequestException(this.getResult());
        }
        return this;
    }
}
