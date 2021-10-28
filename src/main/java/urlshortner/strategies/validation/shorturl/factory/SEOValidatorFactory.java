package urlshortner.strategies.validation.shorturl.factory;

import urlshortner.strategies.validation.shorturl.FixedLengthISEOValidationStrategy;
import urlshortner.strategies.validation.shorturl.ISEOValidationStrategy;
import urlshortner.strategies.validation.shorturl.MaxLengthSEoValidationStrategy;
import urlshortner.strategies.validation.shorturl.enums.SEOValidationStrategy;

import java.util.HashMap;
import java.util.Map;

public class SEOValidatorFactory {

    private static final Map<SEOValidationStrategy, ISEOValidationStrategy> VALIDATORS = new HashMap<>();

    static {
        VALIDATORS.put(SEOValidationStrategy.FIXED_LENGTH, new FixedLengthISEOValidationStrategy());
        VALIDATORS.put(SEOValidationStrategy.MAX_LENGTH, new MaxLengthSEoValidationStrategy());
    }

    public ISEOValidationStrategy get(SEOValidationStrategy strategy) {
        return VALIDATORS.get(strategy);
    }
}
