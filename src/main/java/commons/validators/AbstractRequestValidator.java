package commons.validators;

import commons.exceptions.GameException;
import commons.exceptions.InvalidRequestException;

public abstract class AbstractRequestValidator<Request> extends AbstractValidator<Request> {
    @Override
    public IValidator<Request> validate(final Request request) {
        if (request == null) {
            this.putMessage("request", MUST_NOT_BE_EMPTY, "request");
            return this;
        }
        return this;
    }

    @Override
    public IValidator<Request> validateOrThrowException(final Request request) throws GameException {
        this.validate(request);
        if (this.hasMessages()) {
            this.throwException();
        }
        return this;
    }

    protected void throwException() {
        throw new InvalidRequestException(this.getResult());
    }
}
