package chat.entities;

import commons.entities.AbstractEntity;

public class Message extends AbstractEntity<Integer> {

    private User sender;
    private User recipient;
    private String content;
    private final Long sentTimestamp;
    private Long timestamp;

    private Message(final Integer id, final User sender, final User recipient,
                    final String content,  final Long sentTimestamp, final Long timestamp) {
        super(id);
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.sentTimestamp = sentTimestamp;
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSentTimestamp() {
        return sentTimestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                super.toString() +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", content='" + content + '\'' +
                ", sentTimestamp=" + sentTimestamp +
                ", timestamp=" + timestamp +
                '}';
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder extends AbstractEntityBuilder<Message, Integer, MessageBuilder> {

        private User sender;
        private User recipient;
        private String content;
        private Long sentTimestamp;
        private Long timestamp;

        public MessageBuilder withSender(final User sender) {
            this.sender = sender;
            return this;
        }

        public MessageBuilder withRecipient(final User recipient) {
            this.recipient = recipient;
            return this;
        }

        public MessageBuilder withContent(final String content) {
            this.content = content;
            return this;
        }


        public MessageBuilder withSentTimestamp(final Long timestamp) {
            this.sentTimestamp = timestamp;
            return this;
        }

        public MessageBuilder withTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public Message build() {
            return new Message(this.id, this.sender, this.recipient, this.content,
                               this.sentTimestamp, this.timestamp);
        }
    }
}
