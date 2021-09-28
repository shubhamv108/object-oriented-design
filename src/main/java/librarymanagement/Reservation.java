package librarymanagement;

import hotelbooking.entities.ReservationStatus;
import librarymanagement.actors.Member;
import librarymanagement.book.BookItem;

import java.util.Date;
import java.util.List;

public class Reservation {

    List<BookItem> bookItems;
    Date createdAt;
    Member member;
    ReservationStatus status;

}
