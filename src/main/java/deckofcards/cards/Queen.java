package deckofcards.cards;

import deckofcards.suits.Suit;

public class Queen extends Card {
    public Queen(final Suit suit) {
        this(12, suit);
    }
    public Queen(final int value, final Suit suit) {
        super(value, suit);
    }
}
