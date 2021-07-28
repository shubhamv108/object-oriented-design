package logginglibrary.api;

import commons.builder.IBuilder;
import logginglibrary.constants.LogLevel;
import logginglibrary.utils.DateUtil;

import java.util.Date;

public class Message {

    private final String content;
    private final LogLevel logLevel;
    private String threadName;
    private final String namespace;
    private transient Date date;
    private String timeStamp;

    public Message(final String content, final LogLevel logLevel, final String threadName,
                   final String namespace) {
        this.content = content;
        this.logLevel = logLevel;
        this.threadName = threadName;
        this.namespace = namespace;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setTimeStampWithFormat(final String timeFormat) {
        this.timeStamp = DateUtil.getFormattedDate(this.date, timeFormat);
    }

    public Date getDate() {
        return this.date;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] [%s] [%s] [%s] %s\n",
                this.timeStamp, this.logLevel, this.namespace, this.threadName, this.content);
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder implements IBuilder<Message> {

        private String content;
        private LogLevel logLevel;
        private String threadName;
        private String namespace;

        public MessageBuilder withContent(final String content) {
            this.content = content;
            return this;
        }

        public MessageBuilder withLogLevel(final LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public MessageBuilder withThread(final String threadName) {
            this.threadName = threadName;
            return this;
        }

        public MessageBuilder withNamespace(final String namespace) {
            this.namespace = namespace;
            return this;
        }

        @Override
        public Message build() {
            return new Message(this.content, this.logLevel, this.threadName, this.namespace);
        }
    }
}
