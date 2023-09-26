package airlinemanagement.models;

import airlinemanagement.apis.SeatInput;

import java.util.Comparator;
import java.util.Objects;

public class Seat implements Comparable<Seat> {

    private int rowNumber;
    private int colNumber;
    private SeatType seatType;
    private SeatClass seatClass;

    private Aircraft aircraft;

    private static final Comparator<Seat> ROW_COMPARATOR = Comparator.comparingInt(x -> x.rowNumber);
    private static final Comparator<Seat> COL_COMPARATOR  = Comparator.comparingInt(x -> x.colNumber);
    private static final Comparator<Seat> CLASS_COMPARATOR  = (x ,y) -> y.seatClass.ordinal() - x.seatClass.ordinal();
    public static final Comparator<Seat> SEAT_COMPARATOR = CLASS_COMPARATOR.thenComparing(ROW_COMPARATOR).thenComparing(COL_COMPARATOR);

    public Seat(
            final int rowNumber,
            final int colNumber,
            final SeatType seatType,
            final SeatClass seatClass,
            final Aircraft aircraft) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        this.seatType = seatType;
        this.seatClass = seatClass;
        this.aircraft = aircraft;
    }

    public static Seat of(
            final SeatInput input,
            final Aircraft aircraft) {
        return new Seat(
            input.getRowNumber(),
            input.getColNumber(),
            input.getSeatType(),
            input.getSeatClass(),
            aircraft);
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public String getSeatNumber() {
        return rowNumber + "-" + colNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Seat seat = (Seat) o;
        return rowNumber == seat.rowNumber && colNumber == seat.colNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, colNumber);
    }

    @Override
    public String toString() {
        return String.format("%s%s-%s-%s", rowNumber, (char) (colNumber + 64), seatClass, seatType);
    }

    @Override
    public int compareTo(final Seat other) {
        return SEAT_COMPARATOR.compare(this, other);
    }
}
