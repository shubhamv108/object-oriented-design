package notificationservice.notifications;

import notificationservice.messages.APNMessage;

public class APNNotification implements INotification {
    private final APNMessage message;

    public APNNotification(APNMessage message) {
        this.message = message;
    }


    @Override
    public boolean send() {
        return false;
    }
}
