package lowercasedecorator;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

public class LowerCaseInputStream extends FilterInputStream {
    protected LowerCaseInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int c = in.read();
        return c == -1 ? c : Character.toLowerCase((char) c);
    }

    @Override
    public int read(byte[] b, int offset, int len) throws IOException {
        int result = in.read(b, offset, len);
        IntStream.range(offset, offset+result).
                forEach(i -> b[i] = (byte) Character.toLowerCase((char) b[i]));
        return result;
    }
}
