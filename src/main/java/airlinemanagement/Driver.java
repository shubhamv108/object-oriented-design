package airlinemanagement;

import airlinemanagement.apis.FlightInstanceView;
import airlinemanagement.apis.FlightSearchFilters;
import airlinemanagement.apis.ReservationRequestItem;
import airlinemanagement.apis.SeatInput;
import airlinemanagement.apis.SeatView;
import airlinemanagement.controllers.AccountController;
import airlinemanagement.controllers.AdminController;
import airlinemanagement.controllers.CustomerController;
import airlinemanagement.models.CreditCardTransaction;
import airlinemanagement.models.Itinerary;
import airlinemanagement.models.Passenger;
import airlinemanagement.models.Seat;
import airlinemanagement.models.SeatClass;
import airlinemanagement.models.SeatType;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Driver {
    public static void main(String[] args) {

        final var adminEmail = "test@test.com";
        new AccountController().create(adminEmail, new char[] {'p', 'a', 's', 's'});
        final var admin = new AdminController().createAdmin(adminEmail);
        final var airportACode = "A";
        final var airportBCode = "B";
        new String(new char[10], 0, 1);
        admin.addAirport(airportACode);
        admin.addAirport(airportBCode);

        final var flightANumber = "A";
        admin.addFlight(flightANumber, airportACode, airportBCode, getFlightABaseFare());

        final var aircraftAName = "A";
        final var aircraftSeatInput = getAircraftSeatInput();
        admin.addAircraft(aircraftAName, aircraftSeatInput);

        final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares = Driver.getSeatFares();

        admin.addWeeklySchedule(
                LocalTime.of(22, 30, 0),
                TimeZone.getTimeZone("Asia/Kolkata"),
                DayOfWeek.FRIDAY,
                aircraftAName,
                seatFares,
                flightANumber);

        final var customerEmail = "customer@test.com";
        new AccountController().create(customerEmail, new char[] {'p', 'a', 's', 's'});
        final var customer = new CustomerController().create(customerEmail);

        final var searchFilters = new FlightSearchFilters(
                airportACode,
                airportBCode,
                2,
                LocalDate.of(2023, 9, 29),
                Period.parse("P1D"),
                List.of(SeatClass.ECONOMY));

        final Collection<FlightInstanceView> flightInstanceViews = customer.search(searchFilters);
        System.out.println(flightInstanceViews);

        final var selectedFlightInstanceView = flightInstanceViews.stream().findFirst().get();

        final SeatView seatView = selectedFlightInstanceView.getSeatView();
        System.out.println(seatView);

        final Itinerary itinerary = customer.createItinerary(airportACode, airportBCode);

        final var passengerSeats = getPassengerSeats(seatView);
        final var reservationRequestItem = new ReservationRequestItem(
                selectedFlightInstanceView.getFlightNumber(), selectedFlightInstanceView.getDepartureInstant(), passengerSeats);
        itinerary.makeReservations(Arrays.asList(reservationRequestItem));

        System.out.println(itinerary.getReservations());

        System.out.println(selectedFlightInstanceView.getSeatView());

        final var payment = itinerary.makePayment();
        payment.performTransaction(new CreditCardTransaction(
                "A",
                "123",
                "01/50",
                payment
        ));
        System.out.println(itinerary.getReservations());

        itinerary.cancel();

        System.out.println(selectedFlightInstanceView.getSeatView());

        itinerary.makeReservations(Arrays.asList(reservationRequestItem));

        System.out.println(itinerary.getReservations());

        System.out.println(selectedFlightInstanceView.getSeatView());

        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(selectedFlightInstanceView.getSeatView());
    }

    private static List<SeatInput> getAircraftSeatInput() {
        return List.of(
            new SeatInput(1, 1, SeatType.REGULAR, SeatClass.ECONOMY),
            new SeatInput(1, 2, SeatType.REGULAR, SeatClass.ECONOMY),
            new SeatInput(1, 3, SeatType.REGULAR, SeatClass.ECONOMY),
            new SeatInput(2, 1, SeatType.REGULAR, SeatClass.ECONOMY),
            new SeatInput(2, 2, SeatType.REGULAR, SeatClass.ECONOMY),
            new SeatInput(2, 3, SeatType.REGULAR, SeatClass.ECONOMY)
        );
    }

    private static Map<SeatClass, BigDecimal> getFlightABaseFare() {
        return new HashMap<>() {
            {
                put(SeatClass.ECONOMY, BigDecimal.valueOf(10000.00));
            }
        };
    }

    private static Map<SeatClass, Map<SeatType, BigDecimal>> getSeatFares() {
        return new HashMap<>() {
            {
                put(SeatClass.ECONOMY, new HashMap<>() {
                    {
                        put(SeatType.REGULAR, BigDecimal.valueOf(100.00));
                    }
                });
            }
        };
    }



    private static Map<Passenger, Seat> getPassengerSeats(final SeatView seatView) {
        final var passengerA = new Passenger("A", "ID", "Passport", new Date(2023, 0, 25));
        final var passengerB = new Passenger("B", "ID", "Passport", new Date(2023, 0, 25));
        final Map<Passenger, Seat> passengerSeats = new HashMap<>();
        passengerSeats.put(passengerA, seatView.getRandomSeat());
        passengerSeats.put(passengerB, seatView.getRandomSeat());
        return passengerSeats;
    }
}
