package logginglibrary.configurations;

import logginglibrary.sinks.ISink;

public class LogLevelSinkConfiguration {

    private final String timeFormat;
    private final ISink sink;

    private LogLevelSinkConfiguration(final String timeFormat, final ISink sink) {
        this.timeFormat = timeFormat;
        this.sink = sink;
    }

    public static LogLevelSinkConfiguration of(final String timeFormat, final ISink sink) {
        return new LogLevelSinkConfiguration(timeFormat, sink);
    }

    public String getTimeFormat() {
        return this.timeFormat;
    }

    public ISink getSink() {
        return this.sink;
    }
}
