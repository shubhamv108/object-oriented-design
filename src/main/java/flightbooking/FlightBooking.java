package flightbooking;

import airlinemanagement.models.Seat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Fn
 * 1. User should be able to search flights based on arrival = destination+date of travel
 * 2. Select flights based on time/preference
 * 3. Enter personal details
 * 4. Make payment
 * 5. Confirmation mail /notification
 *
 * Non fn
 * 1. Modular
 * 2. Extensible
 * 3. Adhere to SOLID
 * 4. Scalable
 *
 * Actors
 * Admin
 * Customers (Users)
 * Airline Authority (Operators)
 *
 * CoreEntities
 * User
 * Flight
 * SeatType
 * Seat
 * Airline
 * Airport
 * Booking
 * Payment
 * NotificationService
 */
public class FlightBooking {
    public class User {}
    public enum SeatType {
        BUSINESS, ECONOMY
    }
    public enum SubSeatType {}
    public class Seat {
        private final int row;
        private final int col;
        private final SeatType seatType;
        private final SubSeatType subSeatType;
        public Seat(int row, int col, SeatType seatType, SubSeatType subSeatType) {
            this.row = row;
            this.col = col;
            this.seatType = seatType;
            this.subSeatType = subSeatType;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Seat seat = (Seat) o;
            return row == seat.row && col == seat.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
    public class Flight {
        private final String flightNumber;
        private final Airline airline;
        private final Map<SeatType, Integer> capacity = new HashMap<>();
        private final Set<Seat> seats = new HashSet<>();
        private final Map<SubSeatType, Double> basePrices = new HashMap<>();
        public Flight(String flightNumber, Airline airline) {
            this.flightNumber = flightNumber;
            this.airline = airline;
        }
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Flight flight = (Flight) o;
            return Objects.equals(flightNumber, flight.flightNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(flightNumber);
        }
    }
    public class Airline {
        private final String name;
        private final Set<Flight> flights = new LinkedHashSet<>();
        public Airline(String name) {
            this.name = name;
        }
    }
    public class Airport {}
    private enum FlightScheduleStatus {
        DELAY, ON_TIME, CANCEL
    }
    public class Schedule {
        private final Flight flight;
        private final Airport source;
        private final Airport destination;
        private final Instant start;
        private final Instant end;
        private FlightScheduleStatus status;
        private final Map<SubSeatType, Double> prices = new HashMap<>();
        public Schedule(Flight flight, Airport source, Airport destination, Instant start, Instant end) {
            this.flight = flight;
            this.source = source;
            this.destination = destination;
            this.start = start;
            this.end = end;
        }
    }
    public class Booking {}
    public class Payment {}
    public class NotificationService {}
}
