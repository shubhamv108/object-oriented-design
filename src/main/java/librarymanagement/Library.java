package librarymanagement;

import librarymanagement.actors.Librarian;
import librarymanagement.book.Book;
import librarymanagement.book.BookItem;

import java.util.List;

public class Library {
    String id;
    List<BookItem> bookItems;
    List<Librarian> librarians;
    Address address;
}
