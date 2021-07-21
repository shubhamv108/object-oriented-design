package commons.notification;

import java.util.Date;

public class SMS extends Notification {

    private final String phoneNumber;

    public SMS(final String notificationId, final Date createdOn, final String content, final String phoneNumber) {
        super(notificationId, createdOn, content);
        this.phoneNumber = phoneNumber;
    }
}
