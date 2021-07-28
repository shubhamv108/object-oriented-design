package loggerlibrary.factories;

import loggerlibrary.configurations.LoggerConfiguration;
import loggerlibrary.configurations.LoggerConfigurationInput;
import loggerlibrary.loggers.ILogger;
import loggerlibrary.loggers.implementations.AsynchronousLogger;
import loggerlibrary.loggers.implementations.SimpleLogger;

import java.util.List;
import java.util.Optional;

public class LoggerFactory {

    private final LoggerConfigurationFactory configurationFactory;

    public LoggerFactory() {
        this(null);
    }

    public LoggerFactory(final LoggerConfigurationFactory configurationFactory) {
        this.configurationFactory = Optional.ofNullable(configurationFactory)
                .orElse(new LoggerConfigurationFactory());
    }

    public ILogger get(final List<LoggerConfigurationInput> configurationInputs) {
        LoggerConfiguration configuration = this.configurationFactory.get(configurationInputs);
        return new SimpleLogger(configuration);
    }

    public ILogger getAsynchronous(final List<LoggerConfigurationInput> configurationInputs) {
        LoggerConfiguration configuration = this.configurationFactory.get(configurationInputs);
        return new AsynchronousLogger(configuration);
    }
}
