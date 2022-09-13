package urlshortner2;

public interface IGenerateShortUrlStrategy {

    String generate(String originalUrl, Integer shortUrlLength);

}
