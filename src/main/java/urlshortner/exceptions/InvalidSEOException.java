package urlshortner.exceptions;

public class InvalidSEOException extends RuntimeException {
    public InvalidSEOException(String seo) {
        super(String.format("Invalid SEO: %s", seo));
    }
}
