package commons.notification;

import java.util.Date;

public class PushNotification extends Notification {
    private final String clientId;
    public PushNotification(final String notificationId, final Date createdOn,
                            final String content, final String clientId) {
        super(notificationId, createdOn, content);
        this.clientId = clientId;
    }
}
