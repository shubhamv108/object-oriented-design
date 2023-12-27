package movieticket;

import java.math.BigDecimal;
import java.util.*;

public class Show {
    Movie movie;
    Date start;
    Date end;

    CinemaHall hall;

    List<Booking> bookings = new ArrayList<>();

    Map<SeatType, BigDecimal> seatprice = new HashMap<>();
}
