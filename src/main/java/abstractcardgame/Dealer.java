package abstractcardgame;

import deckofcards.cards.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Dealer {

    private ArrayList<Card> cards = new ArrayList<>();
    private HashSet<Card> removedCards = new HashSet<>();
    private Game game;
    private Random random = new Random();

    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Card deal() {
        Card card = this.cards.remove(0);
        this.removedCards.add(card);
        return card;
    }

    public void returnCard(final Card card) {
        if (this.removedCards.contains(card)) {
            this.removedCards.remove(card);
            this.cards.add(card);
        }
    }

    public void shuffle() {
        int from = this.randomNumber(this.cards.size()/2, this.cards.size());
    }

    private int randomNumber(int min, int max) {
        return (int) Math.random() * (max - min) + min;
    }

}
