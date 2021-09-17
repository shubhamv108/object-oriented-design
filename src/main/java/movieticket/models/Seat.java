package movieticket.models;

public class Seat {

    private String id;
    private final int rowNumber;
    private final int columnNumber;
    private final Auditorium auditorium;
    private SeatType seatType;
    private SeatStatus status;
    private SeatLock seatLock;

    public Seat(String id, int rowNumber, int columnNumber, Auditorium auditorium) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.auditorium = auditorium;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public SeatLock getSeatLock() {
        return seatLock;
    }

    public void setSeatLock(SeatLock seatLock) {
        this.seatLock = seatLock;
    }
}
