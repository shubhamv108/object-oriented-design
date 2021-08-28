package commons.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTests {

    @Test
    public void isInteger_shouldCheckString() {
        assertEquals(StringUtils.isInteger("10"), true);
        assertEquals(StringUtils.isInteger("10.56"), false);
        assertEquals(StringUtils.isInteger("ABD34"), false);
    }
}
