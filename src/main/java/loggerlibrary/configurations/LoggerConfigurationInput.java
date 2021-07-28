package loggerlibrary.configurations;

import commons.builder.AbstractSequenceBuilder;
import loggerlibrary.configurations.models.sinkinfo.ISinkInfo;
import loggerlibrary.constants.LogLevel;
import loggerlibrary.constants.SinkType;

import java.util.Optional;

public class LoggerConfigurationInput {

    private final String timeFormat;
    private final LogLevel logLevel;
    private final SinkType sinkType;
    private final ISinkInfo sinkInfo;

    public LoggerConfigurationInput(final String timeFormat, final LogLevel logLevel,
                                    final SinkType sinkType, final ISinkInfo sinkInfo) {
        this.timeFormat = timeFormat == null || timeFormat.isBlank()
                ? "yyyy-MM-dd_HH:mm:ss:zzzzZ" : timeFormat;
        this.logLevel = Optional.ofNullable(logLevel).orElse(LogLevel.INFO);
        this.sinkType = Optional.ofNullable(sinkType).orElse(SinkType.Console);
        this.sinkInfo = sinkInfo;
    }

    public String getTimeFormat() {
        return this.timeFormat;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public SinkType getSinkType() {
        return this.sinkType;
    }

    public ISinkInfo getSinkInfo() {
        return this.sinkInfo;
    }

    public static LoggerConfigurationInputBuilder builder() {
        return new LoggerConfigurationInputBuilder();
    }

    public static class LoggerConfigurationInputBuilder extends AbstractSequenceBuilder<LoggerConfigurationInputBuilder, LoggerConfigurationInput> {
        private String timeFormat;
        private LogLevel logLevel;
        private SinkType sinkType;
        private ISinkInfo sinkInfo;

        public LoggerConfigurationInputBuilder withTimeFormat(final String timeFormat) {
            this.timeFormat = timeFormat;
            return this;
        }

        public LoggerConfigurationInputBuilder withLogLevel(final LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public LoggerConfigurationInputBuilder withSinkType(final SinkType sinkType) {
            this.sinkType = sinkType;
            return this;
        }

        public LoggerConfigurationInputBuilder withSinkInfo(final ISinkInfo sinkInfo) {
            this.sinkInfo = sinkInfo;
            return this;
        }

        @Override
        public LoggerConfigurationInputBuilder and() {
            LoggerConfigurationInput input = new LoggerConfigurationInput(this.timeFormat, this.logLevel, this.sinkType, this.sinkInfo);
            super.add(input);
            this.resetAttributes();
            return this;
        }

        @Override
        protected final void resetAttributes() {
            this.timeFormat = null;
            this.logLevel = null;
            this.sinkType = null;
            this.sinkInfo = null;
        }
    }
}
