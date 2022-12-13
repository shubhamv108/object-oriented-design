//package notificationservice;
//
//import notificationservice.enums.NotificationType;
//import notificationservice.messages.EmailMessage;
//import notificationservice.notifications.EmailNotification;
//import notificationservice.notifications.INotification;
//import notificationservice.notifications.WhatsAppNotification;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class NotificationService {
//
//    private final Map<NotificationType, NotificationWorker> workers = new HashMap<>();
//    public NotificationService() {
//        Arrays.stream(NotificationType.values()).forEach(type -> workers.put(type, new NotificationWorker()));
//        workers.values().stream().forEach(worker -> new Thread().start());
//    }
//
//    public boolean sendNotification(NotificationDomain notificationDomain) {
//
//        INotification notificationObject = null;
//        Message messageAttribute;
//
//        switch(notificationDomain.getType()) {
//
//            case NotificationType.EMAIL:
//                messageAttribute = new EmailMessage("abc@abc.com", notificationDomain.getUser().getEmail(),"Order Detail ...");
//                notificationObject = new EmailNotification();
//                break;
//            case NotificationType.WHATSAPP:
//                notificationObject = new WhatsAppNotification();
////                messageAttribute = new Message("9888888888", notificationDomain.getUser().getAccount().getPhoneNumber(),"Order Detail ...");
//                break;
//        }
//
//        return notificationObject.sendNotification(null);
//        return false;
//    }
//
//}
