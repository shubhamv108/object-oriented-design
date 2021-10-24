package urlshortner.strategies.generate;

import java.util.Random;

public class RandomCharacterGenerateShortUrlStrategy implements IGenerateShortUrlStrategy {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+/=";
    private static final char[] CHARS = CHARACTERS.toCharArray();
    private static final Random random = new Random();

    @Override
    public String generate(String originalUrl, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= length; i++)
            builder.append(CHARS[random.nextInt(65)]);
        return builder.toString();
    }

    @Override
    public void returnShortUrl(String url) {
    }
}
