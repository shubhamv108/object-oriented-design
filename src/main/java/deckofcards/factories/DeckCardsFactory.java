package deckofcards.factories;

import deckofcards.cards.Card;
import deckofcards.suits.Club;
import deckofcards.suits.Diamond;
import deckofcards.suits.Heart;
import deckofcards.suits.Spade;
import deckofcards.suits.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DeckCardsFactory {

    private static final Set<Suit> SUITS =
            new HashSet<>(Arrays.asList(new Spade(), new Heart(), new Club(), new Diamond()));
    private static final SuitCardsFactory SUIT_CARDS_FACTORY = new SuitCardsFactory();

    public ArrayList<Card> createAndGet() {
        return SUITS.stream().
                map(SUIT_CARDS_FACTORY::createAndGet).
                flatMap(Collection::stream).
                collect(Collectors.toCollection(ArrayList::new));
    }

}
