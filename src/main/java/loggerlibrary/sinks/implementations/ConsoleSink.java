package loggerlibrary.sinks.implementations;

import commons.builder.IBuilder;
import loggerlibrary.api.Message;

public class ConsoleSink extends AbstractSink {

    protected ConsoleSink(int id) {
        super(id);
    }

    @Override
    public void writeMessage(final Message message) {
        System.out.print(message.toString());
    }

    public static ConsoleSinkBuilder builder() {
        return new ConsoleSinkBuilder();
    }

    public static class ConsoleSinkBuilder implements IBuilder<ConsoleSink> {

        private int id;

        public ConsoleSinkBuilder withId(final int id) {
            this.id = id;
            return this;
        }

        @Override
        public ConsoleSink build() {
            return new ConsoleSink(this.id);
        }
    }
}
