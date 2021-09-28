package notificationservice;

import bookmyshow.actors.User;
import notificationservice.enums.NotificationType;

public class NotificationDomain {

    String id;
    NotificationType type;
    User user;

    public NotificationType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }
}
