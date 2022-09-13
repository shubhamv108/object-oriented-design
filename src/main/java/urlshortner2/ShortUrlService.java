package urlshortner2;

import java.util.Date;

public class ShortUrlService {
    private Users users;
    private ShortUrls shortUrls;
    private IGenerateShortUrlStrategy shortUrlGenerateStrategy;
    private static final int DEFAULT_SHORT_URL_LENGTH = 7;

    public ShortUrlService(Users users,
                           ShortUrls shortUrls,
                           IGenerateShortUrlStrategy shortUrlGenerateStrategy) {
        this.users = users;
        this.shortUrls = shortUrls;
        this.shortUrlGenerateStrategy = shortUrlGenerateStrategy;
    }

    public String create(String originalUrl, String userName, Date expiryAt) {
        User user = this.users.get(userName);
        return this.create(originalUrl, user, expiryAt);
    }

    private String create(String originalUrl, User user, Date expiryAt) {
        ShortUrl shortUrl = new ShortUrl(originalUrl, expiryAt, user);
        this.generateShortUrl(shortUrl);
        user.addShortUrl(shortUrl);
        return shortUrl.getUrl();
    }

    private void generateShortUrl(ShortUrl shortUrl) {
        String url = this.shortUrlGenerateStrategy.generate(
                shortUrl.getOriginalUrl(), DEFAULT_SHORT_URL_LENGTH);
        shortUrl.setUrl(url);
        try {
            shortUrl = this.shortUrls.add(shortUrl);
        } catch (NotExistingShortUrlException e) {
            this.generateShortUrl(shortUrl);
        }
    }

    private String resolve(String shortUrl) {
        return this.shortUrls.getOriginaltUrl(shortUrl);
    }
}
