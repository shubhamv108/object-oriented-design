package urlshortner2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShortUrls {
    private final Map<String, ShortUrl> shortUrls = new ConcurrentHashMap<>();

    public ShortUrl add(ShortUrl shortUrl) throws NotExistingShortUrlException {
        synchronized (shortUrl.getUrl()) {
            ShortUrl existingUrl = shortUrls.get(shortUrl.getUrl());
            if (existingUrl != null)
                throw new NotExistingShortUrlException(shortUrl.getUrl());
            shortUrls.put(shortUrl.getUrl(), shortUrl);
        }
        return shortUrl;
    }

    public boolean hasKey(String shortUrl) {
        return this.shortUrls.containsKey(shortUrl);
    }

    public String getOriginaltUrl(String shortUrl) {
        ShortUrl existingShortUrl = this.shortUrls.get(shortUrl);
        if (existingShortUrl == null ||
                (existingShortUrl.getExpiryAt() != null &&
                System.currentTimeMillis() > existingShortUrl.getExpiryAt().getTime())) {
            throw new NoShortUrlFoundException();
        }
        return existingShortUrl.getOriginalUrl();
    }
}
