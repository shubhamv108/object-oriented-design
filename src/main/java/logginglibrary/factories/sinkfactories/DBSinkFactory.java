package logginglibrary.factories.sinkfactories;

import logginglibrary.configurations.models.sinkinfo.DBSinkInfo;
import logginglibrary.factories.generators.SinkIdGenerator;
import logginglibrary.sinks.implementations.DBSink;

public class DBSinkFactory extends AbstractSinkFactory<DBSinkInfo, DBSink> {
    @Override
    public DBSink get(final DBSinkInfo dbSinkInfo) {
        return DBSink.builder()
                .withId(SinkIdGenerator.get())
                .build();
    }
}
