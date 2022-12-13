package notificationservice.notifications;

import notificationservice.messages.FCMMessage;

public class FCMNotification implements INotification {
    private final FCMMessage message;

    public FCMNotification(FCMMessage message) {
        this.message = message;
    }


    @Override
    public boolean send() {
        return false;
    }
}
