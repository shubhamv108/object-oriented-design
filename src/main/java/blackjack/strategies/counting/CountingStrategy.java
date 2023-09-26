package blackjack.strategies.counting;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CountingStrategy {

    private int deckCount;

    private double count;
    private int aceCount = 0;
    protected final double[] cardValues;

    public CountingStrategy(
            final int deckCount,
            final double[] cardValues) {
        this.deckCount = deckCount;
        this.cardValues = cardValues;
    }

    public void dealt(final int value) {
        this.aceCount += value == 1 ? 1 : 0;
        this.count += this.cardValues[value - 1];
    }

    public void reset() {
        this.count = 0;
    }

    public double trueCount() {
        return this.count / ((double) this.deckCount);
    }

    public int bettingUnits() {
        return ((int) Math.floor(trueCount())) - 1;
    }

}
