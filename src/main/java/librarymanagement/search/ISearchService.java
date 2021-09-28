package librarymanagement.search;

import librarymanagement.Category;
import librarymanagement.actors.Author;
import librarymanagement.book.BookType;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;

public interface ISearchService {
    List<Book> getByTitle(String title);
    List<Book> getByAuthor(Author author);
    List<Book> getAllIntersectionByAuthors(List<Author> authors);
    List<Book> getBySubject(String subject);
    List<Book> getByCategory(BookType type);
    List<Book> getByCategory(Date publicationDate);
}
