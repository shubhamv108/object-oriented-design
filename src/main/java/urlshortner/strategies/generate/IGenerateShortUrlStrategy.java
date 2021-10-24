package urlshortner.strategies.generate;

public interface IGenerateShortUrlStrategy {

    String generate(String originalUrl, int length);

    void returnShortUrl(String url);

}
