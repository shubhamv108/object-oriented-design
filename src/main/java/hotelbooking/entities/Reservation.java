package hotelbooking.entities;

import airlinemanagement.models.PaymentStatus;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reservation {
    private String id;
    private String hotelId;
    private String userId;

    private List<Room> bookedRooms = new ArrayList<>();

    private BigDecimal amount;

    private Payment payment;

    private Date bookingTime;

    private Date checkInTime;
    private Date checkOutTime;
    private ReservationStatus status;

    public void update(final PaymentStatus status) {
        if (PaymentStatus.COMPLETED.equals(status) && ReservationStatus.PENDING.equals(this.status)) {
            this.updateStatus(ReservationStatus.CONFIRMED);
        }
    }

    public ReservationStatus getStatus() {
        return status;
    }

    private void updateStatus(final ReservationStatus status) {
        this.status = status;
    }
}
