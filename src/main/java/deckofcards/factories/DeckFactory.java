package deckofcards.factories;

import deckofcards.Deck;

public class DeckFactory {

    private static final DeckCardsFactory CARDS_FACTORY = new DeckCardsFactory();

    public Deck createAndGet() {
        return new Deck(CARDS_FACTORY.createAndGet());
    }
}
