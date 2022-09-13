package urlshortner2;

import java.util.Date;

public class ShortUrl {
    private String url;
    private final String originalUrl;
    private Date expiryAt;

    private User user;

    public ShortUrl(String originalUrl) {
        this(originalUrl, null, null);
    }
    public ShortUrl(String originalUrl, Date expiryAt, User user) {
        this.originalUrl = originalUrl;
        this.expiryAt = expiryAt;
        this.user = user;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Date getExpiryAt() {
        return expiryAt;
    }

    public boolean isAssignedToUser() {
        return user != null;
    }
}
