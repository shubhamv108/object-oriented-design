package commons.readers.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public abstract class AbstractInputReader implements IInputReader  {

    protected final BufferedReader reader;

    protected AbstractInputReader(final BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Iterator<String> iterator() {
        return new AbstractInputReaderIterator();
    }

    private class AbstractInputReaderIterator implements Iterator<String> {

        private String next;

        private AbstractInputReaderIterator() {
            this.next = this.getNext();
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public String next() {
            String result = this.next;
            this.next = this.getNext();
            return result;
        }

        private String getNext() {
            String next = null;
            try {
                next = reader.readLine();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            if (next != null) {
                next.trim();
                if (next.length() == 0) {
                    next = this.getNext();
                }
            }
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
