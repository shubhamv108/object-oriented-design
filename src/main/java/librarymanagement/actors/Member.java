package librarymanagement.actors;

import amazon.cart.Cart;
import librarymanagement.book.BookItem;
import librarymanagement.Reservation;

import java.util.List;

public class Member extends AbstractMember {

    Cart cart;
    List<Reservation> reservations;
    MemberCard memberCard;

    public boolean canCheckoutBooks(int countOfBooks) {}
    private int getTotalCheckedOutBooks() {}

    public boolean payFine(BookItem bookItem, Reservation reservation) {}
}
