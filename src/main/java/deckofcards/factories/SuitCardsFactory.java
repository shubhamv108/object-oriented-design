package deckofcards.factories;

import deckofcards.cards.Ace;
import deckofcards.cards.Card;
import deckofcards.cards.Jack;
import deckofcards.cards.King;
import deckofcards.cards.NumberCard;
import deckofcards.cards.Queen;
import deckofcards.suits.Suit;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class SuitCardsFactory {

    public Collection<Card> createAndGet(final Suit suit) {
        Set<Card> cards = new TreeSet<>((a, b) -> a.getValue() - b.getValue());
        cards.add(new Ace(suit));
        cards.add(new King(suit));
        cards.add(new Queen(suit));
        cards.add(new Jack(suit));
        IntStream.rangeClosed(2, 10)
                .mapToObj(value -> new NumberCard(value, suit))
                .forEach(cards::add);
        return cards;
    }
}
