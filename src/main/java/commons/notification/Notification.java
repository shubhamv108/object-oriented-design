package commons.notification;

import java.util.Date;

public abstract class Notification {

    private final String notificationId;
    private final Date createdOn;
    private final String content;

    public Notification(final String notificationId, final Date createdOn, final String content) {
        this.notificationId = notificationId;
        this.createdOn = createdOn;
        this.content = content;
    }
}
