package urlshortner2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {

    private String userName;
    private Map<String, ShortUrl> shortUrls = new ConcurrentHashMap<>();

    public User(String userName) {
        this.userName = userName;
    }

    public void addShortUrl(ShortUrl shortUrl) {
        this.shortUrls.put(shortUrl.getUrl(), shortUrl);
    }
}
