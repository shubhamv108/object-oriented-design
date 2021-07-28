package logginglibrary.factories.sinkfactories;

import logginglibrary.configurations.models.sinkinfo.ConsoleSinkInfo;
import logginglibrary.factories.generators.SinkIdGenerator;
import logginglibrary.sinks.implementations.ConsoleSink;

public class ConsoleSinkFactory extends AbstractSinkFactory<ConsoleSinkInfo, ConsoleSink> {
    @Override
    public ConsoleSink get(final ConsoleSinkInfo consoleSinkInfo) {
        return ConsoleSink.builder()
                .withId(SinkIdGenerator.get())
                .build();
    }
}
