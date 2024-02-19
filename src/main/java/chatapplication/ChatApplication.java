package chatapplication;




public class ChatApplication {

    public static void main(String[] args) {
        final Group group = new Group();

        final User user1 = new User();
        final User user2 = new User();

        group.addObserver(user1);
        group.addObserver(user2);

        user1.sendMessage(group, "pikachu");
    }

}
