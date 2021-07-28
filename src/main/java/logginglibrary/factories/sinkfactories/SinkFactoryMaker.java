package logginglibrary.factories.sinkfactories;

import commons.IFactory;
import logginglibrary.constants.SinkType;

public class SinkFactoryMaker implements IFactory<SinkType, AbstractSinkFactory> {

    @Override
    public AbstractSinkFactory get(final SinkType sinkType) {
        switch (sinkType) {
            case File: return new FileSinkFactory();
            case DB: return new DBSinkFactory();
            case Console: return new ConsoleSinkFactory();
        }
        throw new IllegalArgumentException("Sink Type not supported");
    }
}
