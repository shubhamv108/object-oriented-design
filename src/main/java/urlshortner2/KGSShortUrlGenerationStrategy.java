package urlshortner2;

public class KGSShortUrlGenerationStrategy implements IGenerateShortUrlStrategy {

    private final KeyGenerationService keyGenerationService;

    public KGSShortUrlGenerationStrategy(KeyGenerationService keyGenerationService) {
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    public String generate(String originalUrl, Integer shortUrlLength) {
        return this.keyGenerationService.poll();
    }
}
