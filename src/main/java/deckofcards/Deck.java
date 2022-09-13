package deckofcards;

import deckofcards.cards.Blank;
import deckofcards.cards.Card;
import deckofcards.cards.Joker;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final ArrayList<Card> cards;
    private final ArrayList<Joker> jokers;
    private final ArrayList<Blank> blankCards;
    private final Random random = new Random();

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
        this.jokers = new ArrayList<>();
        this.jokers.add(new Joker());
        this.jokers.add(new Joker());
        this.blankCards = new ArrayList<>();
        this.blankCards.add(new Blank());
    }

    public void shuffle() {
        this.shuffle(1, 52);
    }

    public void shuffleFromStart(final int start) {
        this.shuffle(start, 52);
    }

    public void shuffle(final int end) {
        this.shuffle(1, end);
    }

    public void shuffle(final int start, final int end) {
        for (int position = start - 1; position < end; position++) {
            int randomPosition = position + this.random.nextInt(52 - position);
            this.swap(this.cards, position, randomPosition);
        }
    }

    private void swap(final ArrayList<Card> cards, final int x, final int y) {
        Card t = cards.get(x);
        cards.set(x, cards.get(y));
        cards.set(y, t);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }
}
