package loggerlibrary.factories.sinkfactories;

import loggerlibrary.configurations.models.sinkinfo.FileSinkInfo;
import loggerlibrary.factories.generators.SinkIdGenerator;
import loggerlibrary.sinks.ISink;
import loggerlibrary.sinks.implementations.FileSink;

public class FileSinkFactory extends AbstractSinkFactory<FileSinkInfo, ISink> {
    @Override
    public ISink get(final FileSinkInfo sinkInfo) {
        return FileSink.builder()
                .withId(SinkIdGenerator.get())
                .withFilePath(sinkInfo.getPath())
                .build();
    }
}
