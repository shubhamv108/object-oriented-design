package loggerlibrary.loggers.implementations;

import loggerlibrary.api.Message;
import loggerlibrary.configurations.LoggerConfiguration;
import loggerlibrary.configurations.LogLevelSinkConfiguration;
import loggerlibrary.constants.LogLevel;
import loggerlibrary.loggers.ILogger;
import loggerlibrary.sinks.ISink;

import java.util.Date;
import java.util.List;

public class SimpleLogger implements ILogger {

    private final LoggerConfiguration loggerConfiguration;

    public SimpleLogger(final LoggerConfiguration loggerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
    }

    @Override
    public void log(final Message message) {
        message.setDate(new Date());
        this.log(message.getLogLevel(), message);
    }

    protected void log(final LogLevel logLevel, final Message message) {
        if (logLevel == null) return;
        List<LogLevelSinkConfiguration> sinkConfigurations = this.loggerConfiguration.getSinkConfigurations(logLevel);
        if (sinkConfigurations != null) {
            for (int i = 0; i < sinkConfigurations.size(); i++) {
                LogLevelSinkConfiguration sinkConfiguration = sinkConfigurations.get(i);
                /** enriching message with timestamp before redirecting to sink */
                message.setTimeStampWithFormat(sinkConfiguration.getTimeFormat());
                this.sink(message, sinkConfiguration.getSink());
            }
        }
        this.log(logLevel.getLowerLevel(logLevel), message);
    }

    protected void sink(final Message message, final ISink sink) {
        sink.writeMessage(message);
    }
}
