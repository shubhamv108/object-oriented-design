//package movietivketbookingsystem;
//
//import movieticket.Seat;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * "Design a movie ticket booking system similar to BookMyShow that allows users to browse movies, select theaters and showtimes, book tickets, and manage reservations."
// *
// * Requirements:
// * 1. Users can search for movies by title
// * 2. Users can browse movies playing at a given theater
// * 3. Theaters have multiple screens; all screens share the same seat layout (rows A-Z, seats 0-20)
// * 4. Users can view available seats for a showtime and select specific ones
// * 5. Users can book multiple seats in a single reservation; booking returns a confirmation ID
// * 6. Concurrent booking of the same seat: exactly one succeeds
// * 7. Users can cancel a reservation by confirmation ID, releasing the seats
// *
// * Out of Scope:
// * - Payment processing (assume payment always succeeds)
// * - Variable seat layouts or seat types (all seats identical)
// * - Rescheduling (cancel and rebook instead)
// * - UI / rendering
// */
//public class MovieTicketBookingSystem {
//    public class TicketBookingSystem {
//        private final List<Theater> theaters;
//        private final Map<String, Movie> movies;
//        private final Map<String, List<Show>> cityShows;
//        private final Map<String, List<Show>> movieShows;
//        private final Map<String, Reservation> reservations;
//        private final Map<String, String> reservationIdToSeatHoldId;
//        private final Map<String, Show> shows;
//
//        public TicketBookingSystem(List<Theater> theaters, Map<String, Movie> movies, Map<String, List<Show>> cityShows, Map<String, List<Show>> movieShows, Map<String, Reservation> reservations, Map<String, String> reservationIdToSeatHoldId, Map<String, Show> shows) {
//            this.theaters = theaters;
//            this.movies = movies;
//            this.cityShows = cityShows;
//            this.movieShows = movieShows;
//            this.reservations = reservations;
//            this.reservationIdToSeatHoldId =
//            this.shows = shows;
//        }
//
//        public List<Show> search(String title) {}
//        public List<Show> search(String title, String cityId) {}
//        public List<Show> getShowsAtTheater(Theater theater) {}
//        public Reservation createBooking(Show show, List<Seat> seats) {}
//        public void confirm(Show show, List<Seat> seats) {}
//        public Reservation cancel(String bookingId) {}
//    }
//    public class Theater {
//        private final String id;
//        private final List<Show> shows;
//
//        public Theater(String id, List<Show> shows) {
//            this.id = id;
//            this.shows = shows;
//        }
//
//        public List<Show> getShows(Movie movie) {}
//    }
//    public class Movie {
//        private final String id;
//        private final String name;
//
//        public Movie(String id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//    }
//    public class Show {
//        private final Movie movie;
//        private final Theater theater;
//        private LocalDateTime start;
//        private String screenLabel;
//        private final Map<String, Reservation> bookings;
//        private final Map<String, SeatHold> holds;
//
//        public Show(Movie movie, Theater theater, Map<String, Reservation> bookings, Map<String, SeatHold> holds) {
//            this.movie = movie;
//            this.theater = theater;
//            this.bookings = bookings;
//            this.holds = holds;
//        }
//
//        public List<String> getOccupiedSeats() {};
//        public synchronized String holdSeats(List<String> seatIds, Long ttlInMillis) {}
//        public synchronized void confirm(String holdId, Reservation reservation) {}
//        protected synchronized void cleanupExpiredHolds() {}
//        public synchronized void cancel(Reservation reservation) {}
//        public boolean isAvailable(String seat) {}
//        public Reservation isValid(String seat) {}
//    }
//    public enum BookingStatus {
//        INITIATED, CONFIRMED, CANCELLED
//    }
//    public class Reservation {
//        private final String bookingId;
//        private BookingStatus status;
//        private final Show show;
//        private final List<String> seatIds;
//
//        public Reservation(String bookingId, BookingStatus status, Show show, List<String> seatIds) {
//            this.bookingId = bookingId;
//            this.status = status;
//            this.show = show;
//            this.seatIds = new ArrayList<>(seatIds);
//        }
//    }
//
//    public class SeatHold {
//        private final String id;
//        private final List<String> seats;
//        private final Long expiresAtInMillis;
//
//        public SeatHold(List<String> seats, Long expiresAtInMillis) {
//            this.seats = seats;
//            this.expiresAtInMillis = expiresAtInMillis;
//        }
//    }
//
//}
