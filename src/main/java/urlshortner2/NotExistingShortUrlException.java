package urlshortner2;

public class NotExistingShortUrlException extends Exception {
    public NotExistingShortUrlException(String shortUrl) {
        super(String.format("ShortUrl already exists: ", shortUrl));
    }
}
