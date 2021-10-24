package urlshortner.models;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {

    private final String userName;
    private final Map<String, ShortUrl> shortUrls;

    public User(String userName, List<ShortUrl> shortUrls) {
        this.userName = userName;
        this.shortUrls = new ConcurrentHashMap<>();
    }

    public void addShortUrl(ShortUrl shortUrl) {
        this.shortUrls.put(shortUrl.getShortUrl(), shortUrl);
    }
}
