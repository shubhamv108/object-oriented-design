package commons.writers.factories;

import commons.IFactory;
import commons.readers.input.factories.InputReaderFactory;
import commons.writers.ConsoleWriter;
import commons.writers.FileWriter;
import commons.writers.IWriter;

import java.io.IOException;

public class WriterFactory implements IFactory<String, IWriter> {

    private static class SingletonHolder {
        private static final WriterFactory INSTANCE = new WriterFactory();
    }

    public static WriterFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public IWriter get(String input) throws IOException {
        IWriter writer;
        if (input != null && input.length() > 0) {
            writer = FileWriter.of(input);
        } else {
            writer = new ConsoleWriter();
        }
        return writer;
    }
}
