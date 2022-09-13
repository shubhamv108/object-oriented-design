package deckofcards.cards;

import deckofcards.suits.Suit;

public class Blank extends Card {

    private Integer value;
    private Suit suit;

    public Blank() {
        super(null, null);
    }

    public void setSuit(final Suit suit) {
        this.suit = suit;
    }

    public void setValue(final int value) {
        this.value = value;
    }
}
