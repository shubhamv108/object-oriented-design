package blackjack.strategies.counting;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HiLo extends CountingStrategy {

    public HiLo(final int deckCount) {
        super(deckCount, new double[] {-1, 1, 1, 1, 1, 1, 0, 0, 0, -1, -1, -1, -1});
    }
}
