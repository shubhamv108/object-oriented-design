package deckofcards;

import deckofcards.cards.Blank;
import deckofcards.cards.Card;
import deckofcards.cards.Joker;
import deckofcards.suits.Club;
import deckofcards.suits.Diamond;
import deckofcards.suits.Heart;
import deckofcards.suits.Spade;
import deckofcards.suits.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Deck {
    private final ArrayList<Card> cards;
    private final ArrayList<Joker> jokers;
    private final ArrayList<Blank> blankCards;
    private final Set<Card> removedCards;

    private final Collection<Suit> suits;
    private final Random random = new Random();

    public Deck() {
        this.suits = Collections.unmodifiableCollection(this.createAndGetSuits());
        this.cards = this.suits
                .stream()
                .map(Suit::getCards)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        this.jokers = new ArrayList<>();
        this.jokers.add(new Joker());
        this.jokers.add(new Joker());
        this.blankCards = new ArrayList<>();
        this.blankCards.add(new Blank());
        this.removedCards = new HashSet<>();
    }

    public Deck(final Collection<Suit> suits, final ArrayList<Card> cards, final ArrayList<Joker> jokers,
                final ArrayList<Blank> blankCards, final HashSet<Card> removedCards) {
        this.suits = suits;
        this.cards = cards;
        this.jokers = jokers;
        this.blankCards = blankCards;
        this.removedCards = removedCards;
    }

    public static Deck merge(final Deck deck1, final Deck deck2) {
        List<Suit> suitList = new ArrayList<>(deck1.getSuits());
        suitList.addAll(deck2.getSuits());
        Collection<Suit> suits = Collections.unmodifiableCollection(suitList);
        ArrayList<Card> cards = new ArrayList(deck1.getCards().size() + deck2.getCards().size());
        System.arraycopy(deck1.getCards(), 0, cards, 0, deck1.getCards().size());
        System.arraycopy(deck2.getCards(), 0, cards, deck1.getCards().size(), deck2.getCards().size());
        HashSet<Card> removedCards = new HashSet<>();
        removedCards.addAll(deck1.getRemovedCards());
        removedCards.addAll(deck2.getRemovedCards());
        return new Deck(suits, cards, null, null, removedCards);
    }

    public void cut(int position) {

    }

    public Card removeAndGetFirstCard() {
        Card card = this.cards.remove(0);
        this.removedCards.add(card);
        return card;
    }

    public Card removeAndGetLastCard() {
        Card card = this.cards.remove(this.cards.size() - 1);
        this.removedCards.add(card);
        return card;
    }

    public void shuffle() {
        this.shuffle(1, 52);
    }

    public void shuffleFromStart(final int start) {
        this.shuffle(start, 52);
    }

    public void shuffle(final int end) {
        this.shuffle(1, end);
    }

    public void shuffle(final int start, final int end) {
        for (int position = start - 1; position < end; position++) {
            int randomPosition = position + this.random.nextInt(52 - position);
            this.swap(this.cards, position, randomPosition);
        }
    }

    private void swap(final ArrayList<Card> cards, final int x, final int y) {
        final Card t = cards.get(x);
        cards.set(x, cards.get(y));
        cards.set(y, t);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public Collection<Suit> getSuits() {
        return suits;
    }

    public Set<Card> getRemovedCards() {
        return removedCards;
    }

    private List<Suit> createAndGetSuits() {
        return Arrays.asList(new Spade(), new Heart(), new Club(), new Diamond());
    }
}
