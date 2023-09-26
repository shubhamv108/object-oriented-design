package airlinemanagement.apis;

import airlinemanagement.models.SeatClass;
import airlinemanagement.models.SeatType;

public class SeatInput {
    private int rowNumber;
    private int colNumber;
    private SeatType seatType;
    private SeatClass seatClass;

    public SeatInput(
            final int rowNumber,
            final int colNumber,
            final SeatType seatType,
            final SeatClass seatClass) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        this.seatType = seatType;
        this.seatClass = seatClass;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }
}
