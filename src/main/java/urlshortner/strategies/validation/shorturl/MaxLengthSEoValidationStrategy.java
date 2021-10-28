package urlshortner.strategies.validation.shorturl;

import urlshortner.strategies.validation.shorturl.ISEOValidationStrategy;

public class MaxLengthSEoValidationStrategy implements ISEOValidationStrategy {
    @Override
    public boolean validate(String seo, int length) {
        return seo != null && seo.length() <= length && seo.length() > 0;
    }
}
