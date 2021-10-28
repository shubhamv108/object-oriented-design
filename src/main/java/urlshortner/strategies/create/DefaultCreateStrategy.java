package urlshortner.strategies.create;

import urlshortner.models.ShortUrl;
import urlshortner.models.managers.ShortUrlManager;
import urlshortner.models.User;
import urlshortner.strategies.generate.IGenerateShortUrlStrategy;
import urlshortner.strategies.validation.shorturl.enums.SEOValidationStrategy;

public class DefaultCreateStrategy implements ICreateStrategy {
    private final IGenerateShortUrlStrategy generateShortUrlStrategy;
    private final ShortUrlManager shortenedUrlManager;

    public DefaultCreateStrategy(IGenerateShortUrlStrategy generateShortUrlStrategy,
                                 ShortUrlManager shortenedUrlManager) {
        this.generateShortUrlStrategy = generateShortUrlStrategy;
        this.shortenedUrlManager = shortenedUrlManager;
    }

    @Override
    public ShortUrl handleForExisting(String shortUrl, String originalUrl, Long ttl, User user, int shortUrlLength) {
        return this.shortenedUrlManager.create(
                this.generateShortUrlStrategy.generate(originalUrl, shortUrlLength),
                originalUrl,
                ttl,
                user,
                CreateStrategy.DEFAULT,
                shortUrlLength,
                SEOValidationStrategy.FIXED_LENGTH);
    }
}
