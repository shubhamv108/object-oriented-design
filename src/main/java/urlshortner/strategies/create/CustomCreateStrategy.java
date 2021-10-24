package urlshortner.strategies.create;

import urlshortner.models.ShortUrl;
import urlshortner.models.User;
import urlshortner.exceptions.CustomShortUrlNotAvailableException;

public class CustomCreateStrategy implements ICreateStrategy {
    @Override
    public ShortUrl handleForExisting(String shortUrl, String url, Long ttl, User user, int shortUrlLength) {
        throw new CustomShortUrlNotAvailableException(shortUrl);
    }
}
