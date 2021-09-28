package notificationservice.notifications;

import notificationservice.Message;

public interface INotification {

    boolean sendNotification(Message message);

}
