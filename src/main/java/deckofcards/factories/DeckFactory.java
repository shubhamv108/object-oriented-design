package deckofcards.factories;

import deckofcards.Deck;

public class DeckFactory {

    private static final DeckCardsFactory cardsFactory = new DeckCardsFactory();

    public Deck createAndGet() {
        return new Deck(cardsFactory.createAndGet());
    }
}
