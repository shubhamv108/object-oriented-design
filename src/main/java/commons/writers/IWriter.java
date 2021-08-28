package commons.writers;

import java.io.IOException;
import java.util.List;

public interface IWriter {

    default void appendAll(final List<String> lines) throws IOException {
        for (String line : lines) this.append(line);
    }

    void append(String line) throws IOException;
}
