package commons.validators;

import commons.exceptions.InvalidParameterException;
import commons.utils.StringUtils;
import commons.validators.exceptions.InvalidParametersException;

public abstract class AbstractCommandParamsValidator extends AbstractRequestValidator<String[]> {

    private static final String MUST_BE_AN_INTEGER = "%s must be an integer";
    private static final String MUST_BE_OF_TYPE_INTEGER = "%s must of type integer";

    protected void validateParameterCountOrThrowException(final String[] params, final String[] paramNames) {
        if (params.length < paramNames.length)
            throw new InvalidParameterException(
                    String.format("Expected  parameter %s", String.join(" ", paramNames)));
    }

    protected void validateIntegerParam(final String param, final String paramName) {
        if(!StringUtils.isInteger(param))
            this.putMessage(MUST_BE_OF_TYPE_INTEGER, paramName);
    }

    protected void validateIntegerParam(final String[] params, final String[] paramNames, final int... paramIndexes) {
        for (int paramIndex : paramIndexes)
            this.validateIntegerParam(params[paramIndex], paramNames[paramIndex]);
    }

    protected void validateIntegerParamOrThrowException(final String param, final String paramName) {
        if(!StringUtils.isInteger(param))
            throw new InvalidParameterException(String.format("%s must be an integer", paramName));
    }

    @Override
    protected void throwException() {
        throw new InvalidParametersException(this.getResult());
    }
}
