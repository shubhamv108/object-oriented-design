package airlinereservationsystem.notification;

public class SMSNotification extends Notification {

    final String mobileNumber;

    public SMSNotification(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public void send() {

    }
}
