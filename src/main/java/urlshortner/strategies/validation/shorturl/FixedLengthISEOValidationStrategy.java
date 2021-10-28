package urlshortner.strategies.validation.shorturl;

public class FixedLengthISEOValidationStrategy implements ISEOValidationStrategy {
    @Override
    public boolean validate(String seo, int length) {
        return seo != null && seo.length() == length;
    }
}
