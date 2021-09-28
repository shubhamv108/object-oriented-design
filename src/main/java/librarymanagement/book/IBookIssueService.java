package librarymanagement.book;

import librarymanagement.Reservation;
import librarymanagement.actors.Member;

import java.util.List;

public interface IBookIssueService {

    Reservation getReservationDetails(BookItem bookItem);
    Reservation updateReservation(Reservation reservation);
    public Reservation renew(BookItem item, Member memeber) {}
    public Reservation reserve(List<BookItem> items, Member member) {}
    public Reservation returnBooks(List<BookItem> books, Member member) {}
    public Reservation issueBooks(List<BookItem> books, Member member) {}

}
