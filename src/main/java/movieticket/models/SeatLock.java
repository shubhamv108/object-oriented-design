package movieticket.models;

import java.util.Date;

public class SeatLock {

    int id;
    Seat seat;
    MovieShow show;
    ShowSeatStatus status;
    Customer lockedBy;
    Date lockedAt;
    long timeoutInMilliSeconds;

}
