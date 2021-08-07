package chat.facades;

import chat.entities.Message;
import chat.entities.User;
import chat.managers.IUserManager;
import chat.managers.implementations.UserManager;
import chat.repositories.implementations.MessageRepository;
import commons.facade.IFacade;

import java.util.List;
import java.util.Optional;

public class ChatServer implements IFacade {

    private final IUserManager userManager;
    private final MessageRepository messageRepository;

    private ChatServer() {
        this(UserManager.getInstance(), MessageRepository.getInstance());
    }

    private ChatServer(final IUserManager userManager, final MessageRepository messageRepository) {
        this.userManager = userManager;
        this.messageRepository = messageRepository;
    }

    public static ChatServer getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static final class SingletonHolder {
        private static final ChatServer INSTANCE = new ChatServer();
    }

    public User create(final User user) {
        return this.userManager.create(user);
    }

    public List<Message> getAllUnRetrievedMessages(final User user, final Message lastRetrievedMessage) {
        Integer lastRetrievedMessageId = Optional.ofNullable(lastRetrievedMessage).map(m -> m.getId()).orElse(0);
        return this.messageRepository.fetchAllByReceiverUserIdAndAfterMessageId(
                user.getId(), lastRetrievedMessageId);
    }

    public List<User> getAllUsersByMobileNumber(final List<String> mobileNumbers) {
        return this.userManager.getByMobileNumbers(mobileNumbers);
    }

    public void sendMessage(final Message message) {
        Optional.ofNullable(message.getSender()).map(User::getId)
                .orElseThrow(()-> new RuntimeException("senderId must not be empty"));
        Optional.ofNullable(message.getRecipient()).map(User::getId)
                .orElseThrow(()-> new RuntimeException("recipientId must not be empty"));
//        message.setSender(this.userManager.get(message.getSender().getId()));
//        message.setRecipient(this.userManager.get(message.getRecipient().getId()));
        message.setTimestamp(System.nanoTime());
        this.messageRepository.save(message);
    }
}
