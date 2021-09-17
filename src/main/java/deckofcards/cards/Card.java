package deckofcards.cards;

import deckofcards.suits.Suit;

public abstract class Card {
    protected Integer value;
    protected Suit suit;

    protected Card(final Integer value, final Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }
}
