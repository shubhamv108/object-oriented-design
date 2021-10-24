package urlshortner.strategies.create;

import urlshortner.models.ShortUrl;
import urlshortner.models.User;

public interface ICreateStrategy {

    ShortUrl handleForExisting(String shortUrl, String url, Long ttl, User user, int shortUrlLength);

}
