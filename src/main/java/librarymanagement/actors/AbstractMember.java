package librarymanagement.actors;

import librarymanagement.book.BookItem;
import librarymanagement.Reservation;
import librarymanagement.search.ISearchService;

import java.util.List;

public abstract class AbstractMember extends User {
    ISearchService searchService;

    public Reservation renew(BookItem item) {}
    public Reservation reserve(List<BookItem> items) {}
    public Reservation returnBooks(List<BookItem> books) {}
    public Reservation issueBooks(List<BookItem> books) {}
}
