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
    private final Color color;

    public Suit(final Color color) {
        this.color = color;
    }
}
