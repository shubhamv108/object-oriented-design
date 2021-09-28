package librarymanagement;

import librarymanagement.actors.Member;
import librarymanagement.book.BookItem;

public interface IFineService {

    Fine calculateFine(BookItem item, Member member, int days);

}
