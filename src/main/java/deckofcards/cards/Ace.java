package deckofcards.cards;

import deckofcards.suits.Suit;

public class Ace extends Card {

    public Ace(final Suit suit) {
        this(1, suit);
    }

    protected Ace(final int value, final Suit suit) {
        super(value, suit);
    }
}
