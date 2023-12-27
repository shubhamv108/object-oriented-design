package kubernetes.watcher;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public class Watch<T> implements Watchable<T>, Closeable {

    public static class Response<T> {
        public String type;
        public T object;

        public Status status;

        public Response() {}

        public Response(final String type, final T object) {
            this.type = type;
            this.object = object;
            this.status = null;
        }

        public Response(final String type, final Status status) {
            this.type = type;
            this.object = null;
            this.status = status;
        }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public Iterator<Watch.Response<T>> iterator() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Watch.Response<T> next() {
        return null;
    }
}
