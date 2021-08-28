package commons.readers.input.factories;

import commons.IFactory;
import commons.readers.input.ConsoleInputReader;
import commons.readers.input.FileInputReader;
import commons.readers.input.IInputReader;

public class InputReaderFactory implements IFactory<String, IInputReader> {

    private static class SingletonHolder {
        private static final InputReaderFactory INSTANCE = new InputReaderFactory();
    }

    public static InputReaderFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public IInputReader get(String input) {
        IInputReader reader;
        if (input == null ||  input.length() == 0) {
            reader = new ConsoleInputReader();
        } else {
            reader = FileInputReader.of(input);
        }
        return reader;
    }
}
