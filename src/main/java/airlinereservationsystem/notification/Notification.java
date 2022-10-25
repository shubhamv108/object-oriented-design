package airlinereservationsystem.notification;

import java.util.Date;

public abstract class Notification {
    Date createdAt;
    String content;

    public abstract void send();
}
