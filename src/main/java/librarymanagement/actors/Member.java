package librarymanagement.actors;

import amazon.cart.Cart;
import librarymanagement.book.BookItem;
import librarymanagement.Reservation;

import java.util.List;

public class Member extends AbstractMember {

    Cart cart;
    List<Reservation> reservations;
    MemberCard memberCard;

    public boolean canCheckoutBooks(int countOfBooks) { return false; }
    private int getTotalCheckedOutBooks() { return -1; }

    public boolean payFine(BookItem bookItem, Reservation reservation) { return false; }
}
