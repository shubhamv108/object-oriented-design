package notificationservice.notifications;

import notificationservice.Message;

public class EmailNotification implements INotification {
    @Override
    public boolean sendNotification(Message message) {
        return false;
    }
}
