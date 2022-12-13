package notificationservice;

import notificationservice.notifications.INotification;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationWorker implements Runnable {
    private final Queue<INotification> notifications = new LinkedBlockingQueue<>();

    public void add(INotification notification) {
        this.notifications.add(notification);
    }

    @Override
    public void run() {
        INotification notification = this.notifications.poll();
        if (!notification.send()) {
            this.notifications.add(notification);
        }
    }
}
