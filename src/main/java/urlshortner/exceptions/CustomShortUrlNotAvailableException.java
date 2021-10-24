package urlshortner.exceptions;

public class CustomShortUrlNotAvailableException extends RuntimeException {

    public CustomShortUrlNotAvailableException(String shortUrl) {
        super(String.format("Custom Short url: %s is not available.", shortUrl));
    }

}
