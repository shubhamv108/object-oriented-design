package airlinereservationsystem.notification;

public class EmailNotification extends Notification {

    final String email;

    public EmailNotification(String email) {
        this.email = email;
    }


    @Override
    public void send() {

    }
}
