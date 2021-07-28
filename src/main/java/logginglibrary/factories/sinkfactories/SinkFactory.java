package logginglibrary.factories.sinkfactories;

import logginglibrary.configurations.models.sinkinfo.ConsoleSinkInfo;
import logginglibrary.configurations.models.sinkinfo.DBSinkInfo;
import logginglibrary.configurations.models.sinkinfo.FileSinkInfo;
import logginglibrary.configurations.models.sinkinfo.ISinkInfo;
import logginglibrary.constants.SinkType;
import logginglibrary.sinks.ISink;
import logginglibrary.sinks.implementations.ConsoleSink;
import logginglibrary.sinks.implementations.DBSink;
import logginglibrary.sinks.implementations.FileSink;

public class SinkFactory {

    private final SinkFactoryMaker sinkFactoryMaker = new SinkFactoryMaker();
    private final AbstractSinkFactory<FileSinkInfo, FileSink> fileSinkFactory = this.sinkFactoryMaker.get(SinkType.File);
    private final AbstractSinkFactory<DBSinkInfo, DBSink> dBSinkFactory = this.sinkFactoryMaker.get(SinkType.DB);
    private final AbstractSinkFactory<ConsoleSinkInfo, ConsoleSink> consoleSinkFactory = this.sinkFactoryMaker.get(SinkType.Console);

    public ISink get(final SinkType sinkType, final ISinkInfo sinkInfo) {
        switch (sinkType) {
            case File: return this.get((FileSinkInfo) sinkInfo);
            case DB: return this.get((DBSinkInfo) sinkInfo);
            case Console: return this.get((ConsoleSinkInfo) sinkInfo);
        }
        return null;
    }

    public ISink get(final FileSinkInfo sinkInfo) {
        return this.fileSinkFactory.get(sinkInfo);
    }

    public ISink get(final DBSinkInfo sinkInfo) {
        return this.dBSinkFactory.get(sinkInfo);
    }

    public ISink get(final ConsoleSinkInfo sinkInfo) {
        return this.consoleSinkFactory.get(sinkInfo);
    }

}
