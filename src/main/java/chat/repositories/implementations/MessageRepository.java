package chat.repositories.implementations;

import chat.entities.Message;
import commons.repositories.AbstractOrderedKVStoreRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MessageRepository extends AbstractOrderedKVStoreRepository<Integer, Message> {

    public static MessageRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static final class SingletonHolder {
        private static final MessageRepository INSTANCE = new MessageRepository();
    }

    public List<Message> fetchAllByReceiverUserIdAndAfterMessageId(final String receiverUserId,
                                                                   final Integer messageId) {
        return this.getAllValuesAfterKey(messageId)
                .stream()
                .filter(message -> receiverUserId.equals(message.getRecipient().getId()))
                .collect(Collectors.toList());
    }
}
