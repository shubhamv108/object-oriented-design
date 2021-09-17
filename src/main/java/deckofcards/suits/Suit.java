package deckofcards.suits;

import deckofcards.cards.Ace;
import deckofcards.cards.Card;
import deckofcards.cards.Jack;
import deckofcards.cards.King;
import deckofcards.cards.NumberCard;
import deckofcards.cards.Queen;
import deckofcards.enums.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

public abstract class Suit {
    private final Collection<Card> cards;
    private final Color color;

    public Suit(final Color color) {
        this.color = color;
        this.cards = Collections.unmodifiableSet(this.populateSuitCards(color));
    }

    public Collection<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    private Set<Card> populateSuitCards(final Color color) {
        Set<Card> cards = new TreeSet<>((a, b) -> a.getValue() - b.getValue());
        cards.add(new Ace(this));
        cards.add(new King(this));
        cards.add(new Queen(this));
        cards.add(new Jack(this));
        IntStream.rangeClosed(2, 10)
                .mapToObj(value -> new NumberCard(value, this))
                .forEach(cards::add);
        return cards;
    }
}
