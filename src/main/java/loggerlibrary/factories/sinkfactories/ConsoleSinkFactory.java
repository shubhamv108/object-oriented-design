package loggerlibrary.factories.sinkfactories;

import loggerlibrary.configurations.models.sinkinfo.ConsoleSinkInfo;
import loggerlibrary.factories.generators.SinkIdGenerator;
import loggerlibrary.sinks.implementations.ConsoleSink;

public class ConsoleSinkFactory extends AbstractSinkFactory<ConsoleSinkInfo, ConsoleSink> {
    @Override
    public ConsoleSink get(final ConsoleSinkInfo consoleSinkInfo) {
        return ConsoleSink.builder()
                .withId(SinkIdGenerator.get())
                .build();
    }
}
