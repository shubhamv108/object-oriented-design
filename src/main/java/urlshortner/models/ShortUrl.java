package urlshortner.models;

import java.util.Date;

public class ShortUrl {

    private final String shortUrl;
    private final String originalUrl;
    private final Date createdAt;
    private final Date expirationAt;
    private final User user;

    public ShortUrl(String shortUrl, String originalUrl, Long ttl, User user) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdAt = new Date(System.currentTimeMillis());
        this.expirationAt = new Date(this.createdAt.getTime() + ttl);
        this.user = user;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public Date getExpirationAt() {
        return expirationAt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
