package airlinemanagement.apis;

import airlinemanagement.models.Seat;
import airlinemanagement.models.SeatClass;
import airlinemanagement.models.SeatType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class SeatView {
    private final List<LinkedList<Seat>> seats = new ArrayList<>();
    private final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares;
    private final Set<String> unavailableSeats;

    public SeatView(
            final Collection<Seat> seats,
            final Map<SeatClass, Map<SeatType, BigDecimal>> seatFares,
            final Set<String> unavailableSeats) {
        this.seatFares = seatFares;
        this.populateSeats2DView(seats);
        this.unavailableSeats = unavailableSeats;
    }

    private void populateSeats2DView(final Collection<Seat> seats) {
        seats.forEach(seat -> {
            while (this.seats.size() <= seat.getRowNumber())
                this.seats.add(new LinkedList<>());
            while (this.seats.get(seat.getRowNumber()).size() <= seat.getColNumber())
                this.seats.get(seat.getRowNumber()).add(null);
            this.seats.get(seat.getRowNumber()).set(seat.getColNumber(), seat);
        });
    }

    public Seat getRandomSeat() {
        final Random random = new Random();
        return randomSeat(this.seats.size(), random);
    }

    /*
    ToDo: Fix random seat selection
     */
    private Seat randomSeat(int count, final Random random ) {
        Seat seat = null;
        try {
            if (this.seats.isEmpty() || count == 0)
                throw new IllegalStateException("No seat counld be assigned. Please try again.");
            int row = random.nextInt(this.seats.size());
            if (this.seats.get(row).size() < 0)
                return randomSeat(count - 1, random);
            int col = random.nextInt(this.seats.get(row).size());
            seat = this.seats.get(row).get(col);
            if (seat == null || unavailableSeats.contains(seat))
                return randomSeat(count - 1, random);
        } catch (IllegalArgumentException exception) {
            return randomSeat(count - 1, random);
        }
        return seat;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final LinkedList<Seat> rowSeats : this.seats) {
            rowSeats.stream()
                    .filter(Objects::nonNull)
                    .collect(
                            () -> builder,
                            (e, seat) -> builder.append(seat)
                                    .append("-")
                                    .append(this.seatFares.get(seat.getSeatClass()).get(seat.getSeatType()))
                                    .append("-")
                                    .append((this.unavailableSeats.contains(seat.getSeatNumber()) ? "Unavailable" : "Available"))
                                    .append(" ")
                            ,
                            StringBuilder::append);
            builder.append("\n");
        }
        return builder.toString();
    }
}
