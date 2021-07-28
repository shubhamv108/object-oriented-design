package logginglibrary.factories;

import commons.IFactory;
import logginglibrary.configurations.LogLevelSinkConfiguration;
import logginglibrary.configurations.LoggerConfiguration;
import logginglibrary.configurations.LoggerConfigurationInput;
import logginglibrary.factories.sinkfactories.SinkFactory;
import logginglibrary.sinks.ISink;

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
