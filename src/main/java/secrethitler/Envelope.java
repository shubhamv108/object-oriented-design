package secrethitler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Envelope {

    private final List<Card> cards = Collections.unmodifiableList(new ArrayList<>(2));

    public void addCard(Card card) {
        if (Objects.isNull(card) && !isValidCard(card)) {
            throw new IllegalArgumentException(String.format("%s must be a valid card", card));
        }

        if (cards.size() < 2) {
            this.cards.add(card);
        }
    }

    public abstract boolean isValidCard(Card card);

}
