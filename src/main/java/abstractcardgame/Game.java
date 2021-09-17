package abstractcardgame;

import deckofcards.Deck;
import deckofcards.cards.Card;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Game {
    private final LinkedHashSet<Deck> decks = new LinkedHashSet<>();
    private final ArrayList<Card> cards = new ArrayList<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private final Dealer dealer = new Dealer();

    public void addDeck(final Deck deck) {
        if (!this.decks.contains(deck)) {
            this.decks.add(deck);
            this.dealer.addCards(deck.getCards());
        }
    }

    public Dealer getDealer() {
        return dealer;
    }
}
