package urlshortner2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

class HashShortUrlStrategyTest {

    HashedShortUrlGenerateStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new HashedShortUrlGenerateStrategy("MD5");
    }

    @Test
    void generate() throws NoSuchAlgorithmException {
        int shortUrlLength = 7;
        String originalUrl = "http://test.com/test";
        String shortUrl1 = strategy.generate(originalUrl, shortUrlLength);
        String shortUrl2 = strategy.generate(originalUrl, shortUrlLength);
        Assertions.assertNotEquals(shortUrl1, shortUrl2);
    }
}