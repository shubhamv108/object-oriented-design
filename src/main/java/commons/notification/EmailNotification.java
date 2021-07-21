package commons.notification;

import java.util.Date;

public class EmailNotification extends Notification {
    private final String email;
    public EmailNotification(final String notificationId, final Date createdOn, final String content, final String email) {
        super(notificationId, createdOn, content);
        this.email = email;
    }
}
