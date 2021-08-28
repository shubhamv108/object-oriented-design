package commons.readers.input;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleInputReader extends AbstractInputReader {

    public ConsoleInputReader() {
        super(new BufferedReader(new InputStreamReader(System.in)));
    }

}
