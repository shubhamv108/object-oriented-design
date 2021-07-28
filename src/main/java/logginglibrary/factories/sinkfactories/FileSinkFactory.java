package logginglibrary.factories.sinkfactories;

import logginglibrary.configurations.models.sinkinfo.FileSinkInfo;
import logginglibrary.factories.generators.SinkIdGenerator;
import logginglibrary.sinks.ISink;
import logginglibrary.sinks.implementations.FileSink;

public class FileSinkFactory extends AbstractSinkFactory<FileSinkInfo, ISink> {
    @Override
    public ISink get(final FileSinkInfo sinkInfo) {
        return FileSink.builder()
                .withId(SinkIdGenerator.get())
                .withFilePath(sinkInfo.getPath())
                .build();
    }
}
