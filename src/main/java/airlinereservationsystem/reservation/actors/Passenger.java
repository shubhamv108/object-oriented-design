package airlinereservationsystem.reservation.actors;

import airlinereservationsystem.flight.instance.FlightSeat;
import airlinereservationsystem.notification.EmailNotification;
import airlinereservationsystem.notification.Notification;
import airlinereservationsystem.notification.SMSNotification;

import java.util.Date;
import java.util.Objects;

public class Passenger implements SeatReserver {

    String name;
    String email;
    String mobileNumber;
    String passportNumber;
    Date dateOfBirth;


    @Override
    public boolean notifyReservedSeat(FlightSeat seat) {
        Notification emailNotificaiton = new EmailNotification(email);
        Notification smsNotificaiton = new SMSNotification(mobileNumber);
        emailNotificaiton.send();
        smsNotificaiton.send();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger passenger)) return false;
        return email.equals(passenger.email) && Objects.equals(mobileNumber, passenger.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, mobileNumber);
    }
}
