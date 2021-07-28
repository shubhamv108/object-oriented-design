package loggerlibrary.factories.sinkfactories;

import loggerlibrary.configurations.models.sinkinfo.DBSinkInfo;
import loggerlibrary.factories.generators.SinkIdGenerator;
import loggerlibrary.sinks.implementations.DBSink;

public class DBSinkFactory extends AbstractSinkFactory<DBSinkInfo, DBSink> {
    @Override
    public DBSink get(final DBSinkInfo dbSinkInfo) {
        return DBSink.builder()
                .withId(SinkIdGenerator.get())
                .build();
    }
}
