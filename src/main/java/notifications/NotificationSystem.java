package notifications;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationSystem {

    class Request {
        final String eventType;
        final String eventName;
        final Optional<String> subject;
        final Optional<String> body;
        final Map<String, String> notificationData;
        final Integer accountId;

        Request(String eventType, String eventName, Optional<String> subject, Optional<String> body, Map<String, String> notificationData, Integer accountId) {
            this.eventType = eventType;
            this.eventName = eventName;
            this.subject = subject;
            this.body = body;
            this.notificationData = notificationData;
            this.accountId = accountId;
        }
    }

    class Account {
        final Integer id;
        final String email;
        final String mobile;

        Account(Integer id, String email, String mobile) {
            this.id = id;
            this.email = email;
            this.mobile = mobile;
        }
    }

    class AccountManager {
        AtomicInteger counter = new AtomicInteger();
        Map<Integer, Account> accounts = new ConcurrentHashMap<>();

        public Optional<Account> getAccount(Integer accountId) {
            return Optional.ofNullable(accounts.get(accountId));
        }

        public Account create(String email, String mobile) {
            int id = counter.incrementAndGet();
            Account account = new Account(id, email, mobile);
            accounts.put(id, account);
            return account;
        }
    }

    enum DeviceType {
        ANDROID, IOS, DESKTOP
    }

    class Device {
        String token;
        DeviceType deviceType;
        Integer accountId;
    }

    class DeviceManager {
        Map<Integer, List<Device>> devices = new ConcurrentHashMap<>();

        public List<Device> getDevices(Integer accountId) {
            return devices.getOrDefault(accountId, Collections.emptyList());
        }

        public void addDevice(Integer accountId, Device device) {
            devices.computeIfAbsent(accountId, __ -> new CopyOnWriteArrayList<>())
                    .add(device);
        }
    }

    enum NotificationType {
        EMAIL,
        SMS,
        APN,
        FCM,
        DESKTOP;

        public static NotificationType getByDevice(DeviceType deviceType) {
            switch (deviceType) {
                case IOS: return APN;
                case ANDROID: return FCM;
                case DESKTOP: return DESKTOP;
            }
            return null;
        }
    }

    class NotificationTemplate {
        String eventType;
        String eventName;
        final String subject;
        final String body;
        final Map<String, Object> meta;

        NotificationTemplate(String subject, String body, Map<String, Object> meta) {
            this.subject = subject;
            this.body = body;
            this.meta = meta;
        }
    }

    class NotificationTemplateManager {
        Map<String, Map<String, NotificationTemplate>> templates = new HashMap<>();

        public NotificationTemplate getTemplate(String eventType, String eventName) {
            return templates
                    .getOrDefault(eventType, Collections.emptyMap())
                    .get(eventName);
        }

        public void put(String eventType, String eventName, NotificationTemplate notificationTemplate) {
            templates
                    .computeIfAbsent(eventType, e -> new HashMap<>())
                    .put(eventName, notificationTemplate);
        }
    }

    class NotificationService {
        final AccountManager accountManager;
        final DeviceManager deviceManager;
        final NotificationTemplateManager notificationTemplateManager;
        final NotificationQueue notificationQueue;

        public NotificationService(AccountManager accountManager, DeviceManager deviceManager, NotificationTemplateManager notificationTemplateManager, NotificationQueue notificationQueue) {
            this.accountManager = accountManager;
            this.deviceManager = deviceManager;
            this.notificationTemplateManager = notificationTemplateManager;
            this.notificationQueue = notificationQueue;
        }

        public void sendNotification(final Request request) {
            NotificationTemplate notificationTemplate = this.notificationTemplateManager.getTemplate(request.eventType, request.eventName);
            String subject = request.subject.isPresent() ? request.subject.get() : PlaceholderUtil.replace(notificationTemplate.subject, request.notificationData);
            String body = request.body.isPresent() ? request.body.get() : PlaceholderUtil.replace(notificationTemplate.body, request.notificationData);

            Account account = accountManager.getAccount(request.accountId)
                    .orElseThrow();

            if (account.mobile != null && !account.mobile.isEmpty())
                notificationQueue.offer(new SMSNotification(subject, body, account.mobile));
            if (account.email != null && !account.email.isEmpty())
                notificationQueue.offer(
                        new EmailNotification(
                                subject,
                                body,
                                (String) notificationTemplate.meta.get("from"),
                                account.email,
                                (List<String>) notificationTemplate.meta.getOrDefault("cc", Collections.emptyList()),
                                (List<String>) notificationTemplate.meta.getOrDefault("bcc", Collections.emptyList())));
            this.deviceManager.getDevices(request.accountId)
                    .stream()
                    .map(device -> new PushNotification(NotificationType.getByDevice(device.deviceType), subject, body, device.token))
                    .forEach(notificationQueue::offer);

        }
    }

    class PlaceholderUtil {
        private static String replace(String message, Map<String, String> values) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            return message;
        }
    }

    abstract class Notification {
        final NotificationType notificationType;
        final String subject;
        final String body;
        private AtomicInteger tries = new AtomicInteger();

        Notification(NotificationType notificationType, String subject, String body) {
            this.notificationType = notificationType;
            this.subject = subject;
            this.body = body;
        }

        public void incrementTries() {
            tries.incrementAndGet();
        }
    }

    class SMSNotification extends Notification {

        final String mobile;

        public SMSNotification(String subject, String body, String mobile) {
            super(NotificationType.SMS, subject, body);
            this.mobile = mobile;
        }
    }

    class EmailNotification extends Notification {
        private final String from;
        private final String to;
        private final List<String> cc;
        private final List<String> bcc;

        public EmailNotification(String subject, String body, String from, String to, List<String> cc, List<String> bcc) {
            super(NotificationType.EMAIL, subject, body);
            this.from = from;
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
        }
    }

    class PushNotification extends Notification {
        final String token;

        PushNotification(NotificationType notificationType, String subject, String body, String token) {
            super(notificationType, subject, body);
            this.token = token;
        }
    }

    class NotificationQueue {
        ConcurrentHashMap<NotificationType, LinkedBlockingDeque<Notification>> queues = new ConcurrentHashMap<>();
        ConcurrentHashMap<NotificationType, LinkedBlockingDeque<Notification>> deadLetterQueues = new ConcurrentHashMap<>();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        public boolean offer(Notification notification) {
            return getQueue(notification.notificationType).offer(notification);
        }

        private LinkedBlockingDeque<Notification> getQueue(NotificationType notificationType) {
            return queues.computeIfAbsent(notificationType, __ -> new LinkedBlockingDeque());
        }

        private LinkedBlockingDeque<Notification> getDLQ(NotificationType notificationType) {
            return deadLetterQueues.computeIfAbsent(notificationType, __ -> new LinkedBlockingDeque());
        }

        public Notification poll(NotificationType notificationType) throws InterruptedException {
            return getQueue(notificationType).take(); // Waits indefinitely until an element is available.
        }

        public boolean offerToRetry(Notification notification) {
            return getDLQ(notification.notificationType).offer(notification);
        }

        private void startReDrive() {
            scheduler.scheduleAtFixedRate(() -> reDrive(), 1000, 1000, TimeUnit.SECONDS);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (scheduler != null)
                    while (!scheduler.isShutdown())
                        scheduler.shutdown();
            }));
        }

        private void reDrive() {
            deadLetterQueues.values().stream().flatMap(LinkedBlockingDeque::stream).forEach(this::offer);
        }
    }

    class NotificationWorker {
        final NotificationQueue notificationQueue;
        final NotificationChannelAdapterFactory notificationChannelAdapterFactory = NotificationChannelAdapterFactory.getInstance();

        ExecutorService scheduler = Executors.newFixedThreadPool(1);

        public NotificationWorker(NotificationQueue notificationQueue, NotificationType notificationType) throws InterruptedException {
            this.notificationQueue = notificationQueue;
            execute(notificationType);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (scheduler != null)
                    while (!scheduler.isShutdown())
                        scheduler.shutdown();
            }));
        }

        private void execute(NotificationType notificationType) throws InterruptedException {
            while (true) {
                Notification notification = notificationQueue.poll(notificationType);
                Optional.ofNullable(notificationChannelAdapterFactory.get(notification.notificationType))
                        .ifPresentOrElse(
                                adapter -> adapter.send(notification),
                                () -> notificationQueue.offerToRetry(notification));
                notification.incrementTries();
            }
        }
    }

    public interface NotificationChannelAdapter {
        void send(Notification notification);
    }

    public static class SMSNotificationAdapter implements NotificationChannelAdapter {
        @Override
        public void send(Notification notification) {
            System.out.println("sending sms to " + ((SMSNotification) notification).mobile);
        }
    }

    public static class EmailNotificationAdapter implements NotificationChannelAdapter {
        @Override
        public void send(Notification notification) {
            System.out.println("sending email from " + ((EmailNotification) notification).from + " to " + ((EmailNotification) notification).to);
        }
    }

    public static class APNNotificationAdapter implements NotificationChannelAdapter {
        @Override
        public void send(Notification notification) {
            System.out.println("sending apn notification token " + ((PushNotification) notification).token);
        }
    }

    public static class FCMNotificationAdapter implements NotificationChannelAdapter {
        @Override
        public void send(Notification notification) {
            System.out.println("sending fcm notification token " + ((PushNotification) notification).token);
        }
    }

    static class NotificationChannelAdapterFactory {
        final Map<NotificationType, NotificationChannelAdapter> notificationChannelAdapters = new ConcurrentHashMap<>();

        private NotificationChannelAdapterFactory() {
            notificationChannelAdapters.put(NotificationType.SMS, new SMSNotificationAdapter());
            notificationChannelAdapters.put(NotificationType.EMAIL, new EmailNotificationAdapter());
            notificationChannelAdapters.put(NotificationType.APN, new APNNotificationAdapter());
            notificationChannelAdapters.put(NotificationType.FCM, new FCMNotificationAdapter());
        }

        public static NotificationChannelAdapterFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final NotificationChannelAdapterFactory INSTANCE = new NotificationChannelAdapterFactory();
        }

        public NotificationChannelAdapter get(NotificationType notificationType) {
            return notificationChannelAdapters.get(notificationType);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NotificationSystem s = new NotificationSystem();
        AccountManager accountManager = s.new AccountManager();
        Account account = accountManager.create("shubhamv.test@gmail.com", "9192119211");
        DeviceManager deviceManager = s.new DeviceManager();
        NotificationTemplateManager notificationTemplateManager = s.new NotificationTemplateManager();
        notificationTemplateManager.put("A", "B", s.new NotificationTemplate("{subjectplaceholder}", "{body}", Map.of("from", "A@a.com")));
        NotificationQueue notificationQueue = s.new NotificationQueue();
        NotificationService notificationService = s.new NotificationService(accountManager, deviceManager, notificationTemplateManager, notificationQueue);
        notificationService.sendNotification(s.new Request("A", "B", Optional.empty(), Optional.empty(), new HashMap<>() {
            {
                put("subjectplaceholder", "testHeader");
                put("body", "content");
            }
        }, account.id));

        new Thread(() -> {
            try {
                s.new NotificationWorker(notificationQueue, NotificationType.SMS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                s.new NotificationWorker(notificationQueue, NotificationType.EMAIL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                s.new NotificationWorker(notificationQueue, NotificationType.FCM);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                s.new NotificationWorker(notificationQueue, NotificationType.APN);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                s.new NotificationWorker(notificationQueue, NotificationType.DESKTOP);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
