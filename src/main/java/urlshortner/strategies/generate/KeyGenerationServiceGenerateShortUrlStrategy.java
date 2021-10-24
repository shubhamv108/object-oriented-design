package urlshortner.strategies.generate;

import urlshortner.models.managers.ShortUrlManager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class KeyGenerationServiceGenerateShortUrlStrategy implements IGenerateShortUrlStrategy {

    private final ConcurrentSkipListSet<String> availableKeys = new ConcurrentSkipListSet();
    private final Set<String> usedKeys = ConcurrentHashMap.newKeySet();
    private final RandomCharacterGenerateShortUrlStrategy shortUrlStrategy =
            new RandomCharacterGenerateShortUrlStrategy();
    private final int defaultShortUrlLength;
    private final ShortUrlManager shortUrlManager;

    public KeyGenerationServiceGenerateShortUrlStrategy(ShortUrlManager shortUrlManager, int shortUrlLength) {
        this.shortUrlManager = shortUrlManager;
        this.defaultShortUrlLength = shortUrlLength;
        this.addNewUrls(shortUrlLength);
    }

    private void addNewUrls(int shortUrlLength) {
        for (int i = 1; i < 1000;) {
            String shortUrl = this.shortUrlStrategy.generate(null, 8);
            if (this.addNew(shortUrl))
                i++;
        }
    }

    private boolean addNew(String shortUrl) {
        synchronized (shortUrl) {
            if (!this.usedKeys.contains(shortUrl) && !this.shortUrlManager.contains(shortUrl))
                return this.availableKeys.add(shortUrl);
        }
        return false;
    }

    private String getNext() {
        if (this.availableKeys.size() < 1000)
            this.addNewUrls(this.defaultShortUrlLength);
        String next = this.availableKeys.pollFirst();
        this.usedKeys.add(next);
        return next;
    }

    @Override
    public String generate(String originalUrl, int length) {
        return this.getNext();
    }

    @Override
    public void returnShortUrl(String url) {
        this.addNew(url);
    }
}
