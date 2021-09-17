package deckofcards.cards;

import deckofcards.suits.Suit;

public class King extends Card {
    public King(final Suit suit) {
        this(13, suit);
    }
    public King(final int value, final Suit suit) {
        super(value, suit);
    }
}
