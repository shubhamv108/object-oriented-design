package urlshortner.models.managers;

import urlshortner.models.ShortUrl;
import urlshortner.models.User;
import urlshortner.strategies.create.CreateStrategy;
import urlshortner.strategies.create.CustomCreateStrategy;
import urlshortner.strategies.create.DefaultCreateStrategy;
import urlshortner.strategies.create.ICreateStrategy;
import urlshortner.strategies.generate.HashBasedGenerateShortUrlStrategy;
import urlshortner.strategies.generate.IGenerateShortUrlStrategy;
import urlshortner.strategies.validation.shorturl.enums.SEOValidationStrategy;
import urlshortner.strategies.validation.shorturl.factory.SEOValidatorFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShortUrlManager {
    private final Map<String, ShortUrl> urls = new ConcurrentHashMap<>();
    private final Map<CreateStrategy, ICreateStrategy> createStrategies;
    private final IGenerateShortUrlStrategy generateShortUrlStrategy;
    private final SEOValidatorFactory seoValidatorFactory;

    public ShortUrlManager() {
        this.seoValidatorFactory = new SEOValidatorFactory();
        this.generateShortUrlStrategy = new HashBasedGenerateShortUrlStrategy();
        Map<CreateStrategy, ICreateStrategy> createStrategies = new HashMap<>();
        createStrategies.put(CreateStrategy.DEFAULT, new DefaultCreateStrategy(this.generateShortUrlStrategy, this));
        createStrategies.put(CreateStrategy.CUSTOM, new CustomCreateStrategy());
        this.createStrategies = Collections.unmodifiableMap(createStrategies);
    }

    public ShortUrl create(String shortUrl, String originalUrl, Long ttl, User user, CreateStrategy createStrategy,
                           int shortUrlLength, SEOValidationStrategy seoValidationStrategy) {
        if (shortUrl == null || shortUrl.isBlank())
            shortUrl = this.generateShortUrlStrategy.generate(originalUrl, shortUrlLength);
        else
            this.seoValidatorFactory.get(seoValidationStrategy).validateOrThrow(shortUrl, shortUrlLength);

        ShortUrl result = null;
        if (this.urls.containsKey(shortUrl))
            synchronized (shortUrl) {
                if (!this.urls.containsKey(shortUrl)) {
                    this.urls.put(shortUrl, result = new ShortUrl(shortUrl, originalUrl, ttl, user));
                    user.addShortUrl(result);
                }
            }

        if (result == null)
            this.createStrategies.get(createStrategy)
                    .handleForExisting(shortUrl, originalUrl, ttl, user, shortUrlLength);
        return result;
    }

    public boolean contains(String shortUrl) {
        return this.urls.containsKey(shortUrl);
    }

    public String getUrl(String shortUrl) {
        ShortUrl result = this.urls.get(shortUrl);
        if (result != null)
            if (System.currentTimeMillis() <= result.getExpirationAt().getTime())
                return result.getOriginalUrl();
            else
                this.urls.remove(shortUrl);
        throw new IllegalArgumentException(String.format("No short url: %s", shortUrl));
    }

    public boolean delete(String shortUrl) {
        ShortUrl result = this.urls.get(shortUrl);
        if (result != null) {
            this.urls.remove(shortUrl);
            this.generateShortUrlStrategy.returnShortUrl(shortUrl);
            return true;
        }
        return false;
    }
}
