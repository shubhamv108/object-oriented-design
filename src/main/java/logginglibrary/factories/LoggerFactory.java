package logginglibrary.factories;

import logginglibrary.configurations.LoggerConfiguration;
import logginglibrary.configurations.LoggerConfigurationInput;
import logginglibrary.constants.LoggerType;
import logginglibrary.loggers.ILogger;
import logginglibrary.loggers.implementations.AsynchronousLogger;
import logginglibrary.loggers.implementations.SimpleLogger;

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
