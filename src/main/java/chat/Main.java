package chat;

import chat.entities.Message;
import chat.entities.User;
import chat.facades.ChatServer;

import java.util.List;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        User userA = User.builder().withName("A").withMobileNumber("1234567890").build();
        User userB = User.builder().withName("B").withMobileNumber("1234567891").build();
        ChatServer.getInstance().create(userA);
        ChatServer.getInstance().create(userB);
        System.out.println(userA);
        System.out.println(userB);

        User recipientUser = ChatServer.getInstance()
                .getAllUsersByMobileNumber(Arrays.asList("1234567891"))
                .get(0);

        Message message =
                Message.builder()
                .withSender(User.builder().withId(userA.getId()).build())
                .withRecipient(User.builder().withId(recipientUser.getId()).build())
                .withContent("Test")
                .withSentTimestamp(System.nanoTime())
                .build();

        ChatServer.getInstance().sendMessage(message);

        List<Message> messages = ChatServer.getInstance().getAllUnRetrievedMessages(
                User.builder().withId(userB.getId()).build(), Message.builder().build()
        );

        System.out.println(messages);

    }

}
