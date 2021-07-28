package loggerlibrary.sinks.implementations;

import loggerlibrary.sinks.ISink;

public abstract class AbstractSink implements ISink {

    private final int id;

    protected AbstractSink(final int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
