package urlshortner2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;

public class HashedShortUrlGenerateStrategy implements IGenerateShortUrlStrategy {

    private final String algorithm;

    public HashedShortUrlGenerateStrategy(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String generate(String originalUrl, Integer shortUrlLength) {
        MessageDigest md5MessageDigest = null;
        try {
            md5MessageDigest = MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md5MessageDigest.digest(originalUrl.getBytes());
        String encoded = Base64.getEncoder().encodeToString(digest);
        char[] chrs = encoded.toCharArray();
        Random random = new Random();
        return IntStream.range(0, shortUrlLength).
                map(i -> chrs[random.nextInt(22) - 1]).
                collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).
                toString();
    }
}
