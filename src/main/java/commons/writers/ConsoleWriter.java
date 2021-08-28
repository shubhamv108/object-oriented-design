package commons.writers;

import java.io.IOException;

public class ConsoleWriter implements IWriter {
    @Override
    public void append(final String line) throws IOException {
        System.out.println(line);
    }
}
