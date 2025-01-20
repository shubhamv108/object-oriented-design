package bms2;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BMS {

    class Movie {
        String id;
        List<Show> shows;
    }

    class Show {
        String id;
        Instant start;
        Instant end;
        Movie movie;
        Hall hall;
    }

    class Hall {
        List<Seat> seats;
    }

    class Seat {
        String id;
        int row;
        int seat;
        Hall hall;
    }

    interface ISearch {
        List<Movie> findByCity(String cityName);
    }

    class Search implements ISearch {
        Map<String, List<Movie>> byCity;

        public List<Movie> findByCity(String cityName) {
            return byCity.get(cityName);
        }
    }

    class SeatLock {
        Seat seat;
        Show show;
        Date createdAt;
        Long ttlInMillis;
        String lockedBy;

        public boolean isLockExpired() {
            return System.currentTimeMillis() > createdAt.getTime() + ttlInMillis;
        }
    }

    class SeatLockService {
        Map<Show, Map<Seat, List<SeatLock>>> locks;

        public synchronized void getLockedSeats(Show show) {

        }

        public synchronized void lockSeats(Show show, List<Seat> seats, String user) {

        }

        public synchronized void unlockSeats(Show show, List<Seat> seats, String user) {

        }


        private boolean isSeatLocked(final Show show, final Seat seat) {
            return true; // implement
        }
    }

    class Booking {
        String id;
        Show show;
        List<Seat> seats;
        String user;
        BookingStatus status = BookingStatus.CREATED;

        public void updateStatus(BookingStatus status) {
            // basis of current status update status
        }
    }

    enum BookingStatus {
        CREATED, CONFIRMED, EXPIRED
    }

    class BookingService {
        Map<String, Booking> bookings;
        SeatLockService seatLockService;

        public Booking createBooking(String user, Show show, List<Seat> seats) {
            return null; // implement
        }

        public Booking confirmBooking(String user, Show show, List<Seat> seats) {
            return null; // implement
        }
    }

    class SeatAvailabilityService {
        List<Seat> getAvailableSeats(Show show) {
            // get all seats
            // remove all booked seats
            // remove all locked seats
            return null;
        }
    }

}
