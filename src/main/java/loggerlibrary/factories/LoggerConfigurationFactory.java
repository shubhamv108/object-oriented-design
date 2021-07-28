package loggerlibrary.factories;

import commons.IFactory;
import loggerlibrary.configurations.LogLevelSinkConfiguration;
import loggerlibrary.configurations.LoggerConfiguration;
import loggerlibrary.configurations.LoggerConfigurationInput;
import loggerlibrary.factories.sinkfactories.SinkFactory;
import loggerlibrary.sinks.ISink;

import java.util.List;

public class LoggerConfigurationFactory implements IFactory<List<LoggerConfigurationInput>, LoggerConfiguration> {

    private final SinkFactory sinkFactory = new SinkFactory();

    @Override
    public LoggerConfiguration get(final List<LoggerConfigurationInput> loggerConfigurationInputs) {
        LoggerConfiguration configuration = new LoggerConfiguration();
        for (int i = 0; i < loggerConfigurationInputs.size(); i++) {
            LoggerConfigurationInput input = loggerConfigurationInputs.get(i);
            ISink sink = this.sinkFactory.get(input.getSinkType(), input.getSinkInfo());
            LogLevelSinkConfiguration config = LogLevelSinkConfiguration.of(input.getTimeFormat(), sink);
            configuration.add(input.getLogLevel(), config);
        }
        return configuration;
    }
}
