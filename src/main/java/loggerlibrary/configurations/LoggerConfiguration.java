package loggerlibrary.configurations;

import loggerlibrary.constants.LogLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoggerConfiguration {

    private final Map<LogLevel, List<LogLevelSinkConfiguration>> logLevelSinkConfigurations;

    public LoggerConfiguration() {
        this(null);
    }

    public LoggerConfiguration(final Map<LogLevel, List<LogLevelSinkConfiguration>> sinks) {
        this.logLevelSinkConfigurations = Optional.ofNullable(sinks).orElse(new HashMap<>());
    }

    public void add(final LogLevel logLevel, final LogLevelSinkConfiguration configuration) {
        List<LogLevelSinkConfiguration> existingConfiguration = this.logLevelSinkConfigurations.get(logLevel);
        if (existingConfiguration == null) {
            this.logLevelSinkConfigurations.put(logLevel, existingConfiguration = new ArrayList<>());
        }
        existingConfiguration.add(configuration);
    }

    public void add(final LogLevel logLevel, final List<LogLevelSinkConfiguration> configuration) {
        List<LogLevelSinkConfiguration> existingConfiguration = this.logLevelSinkConfigurations.get(logLevel);
        if (existingConfiguration == null) {
            this.logLevelSinkConfigurations.put(logLevel, existingConfiguration = configuration);
        } else {
            existingConfiguration.addAll(configuration);
        }
    }

    public void addAll(final Map<LogLevel, List<LogLevelSinkConfiguration>> sinks) {
        sinks.forEach(this::add);
    }

    public List<LogLevelSinkConfiguration> getSinkConfigurations(final LogLevel logLevel) {
        return this.logLevelSinkConfigurations.get(logLevel);
    }
}
