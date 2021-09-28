package librarymanagement.book;

import librarymanagement.Rack;
import librarymanagement.actors.Member;

import java.util.Date;

public class BookItem extends Book {
    Member member;
    String barCode;
    Rack rack;
    Date publicationDate;
    Date issueDate;

    BookStatus status;
    BookFormat format;
}
