package librarymanagement.book;

import librarymanagement.actors.Author;

import java.util.List;

public abstract class Book {
    String id;
    String name;
    List<Author> authors;
    BookType bookType;
}
