package movieticket.models;

import java.util.Set;

public class Booking {

    private String uuid;
    private Customer customer;
    private MovieShow show;
    private Set<Seat> seats;
    private BookingStatus status;

}
