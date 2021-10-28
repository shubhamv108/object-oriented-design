package urlshortner.strategies.validation.shorturl;

import urlshortner.exceptions.InvalidSEOException;

public interface ISEOValidationStrategy {

    boolean validate(String seo, int length);

    default boolean validateOrThrow(String seo, int length) {
        if (!this.validate(seo, length))
            throw new InvalidSEOException(String.format("InvalidSEo: %s", seo));
        return true;
    }

}
