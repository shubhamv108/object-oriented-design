package notificationservice;

import notificationservice.enums.NotificationType;
import notificationservice.notifications.EmailNotification;
import notificationservice.notifications.INotification;
import notificationservice.notifications.SMSNotification;
import notificationservice.notifications.WhatsAppNotification;

public class NotificationService {

    public boolean sendNotification(NotificationDomain notificationDomain) {

        INotification notificationObject;
        Message messageAttribute;

        switch(notificationDomain.getType()) {


            case NotificationType.EMAIL:
                notificationObject = new EmailNotification();
                messageAttribute = new Message("abc@abc.com", notificationDomain.getUser().getAccount().getEmail(),"Order Detail ...");
                break;
            case NotificationType.WHATSAPP:
                notificationObject = new WhatsAppNotification();
                messageAttribute = new Message("9888888888", notificationDomain.getUser().getAccount().getPhoneNumber(),"Order Detail ...");
                break;
            default:
                notificationObject = new SMSNotification();
                messageAttribute = new Message("988888888", notificationDomain.getUser().getAccount().getPhoneNumber(),"Order Detail ...");
                break;
        }

        return notificationObject.sendNotification(messageAttribute);
    }

}
