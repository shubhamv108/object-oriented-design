package blackjack.strategies.counting;

import java.util.Arrays;
import java.util.Collection;

public class OmegaII extends CountingStrategy {

    private int aceCount = 0;

    public OmegaII(final int deckCount) {
        super(deckCount, new double[] { 0 ,1, 1, 2, 2, 2, 1, 0, -1, -2, -2, -2, -2});
    }
}
