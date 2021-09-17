package deckofcards.cards;

import deckofcards.suits.Suit;

public class Jack extends Card {
    public Jack(final Suit suit) {
        super(11, suit);
    }
    public Jack(final Integer value, final Suit suit) {
        super(value, suit);
    }
}
