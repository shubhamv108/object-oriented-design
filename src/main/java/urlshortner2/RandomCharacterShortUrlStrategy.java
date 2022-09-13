package urlshortner2;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomCharacterShortUrlStrategy implements IGenerateShortUrlStrategy {

    private static final String CHARACTERS_STRING = "qwertyuiopasdfghjklzxcvbnmQWETIOPASDFGJKLZXCVBNM1234567890";
    private static final char[] CHARACTERS = CHARACTERS_STRING.toCharArray();

    @Override
    public String generate(String originalUrl, Integer shortUrlLength) {
        Random random = new Random();
        return IntStream.range(0, shortUrlLength).
                map(i -> CHARACTERS[random.nextInt(62) - 1]).
                collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).
                toString();
    }
}
