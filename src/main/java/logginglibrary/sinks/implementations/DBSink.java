package logginglibrary.sinks.implementations;

import commons.builder.IBuilder;
import logginglibrary.api.Message;
import logginglibrary.sinks.ISink;

public class DBSink extends AbstractSink {

    private DBSink(int id) {
        super(id);
    }

    @Override
    public void writeMessage(Message message) {

    }

    public static DBSinkBuilder builder() {
        return new DBSinkBuilder();
    }

    public static class DBSinkBuilder implements IBuilder<DBSink> {

        private int id;

        public DBSinkBuilder withId(final int id) {
            this.id = id;
            return this;
        }

        @Override
        public DBSink build() {
            return new DBSink(this.id);
        }
    }
}
