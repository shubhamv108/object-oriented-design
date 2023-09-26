package blackjack.strategies.counting;

public class VictorAdvancedPointing extends CountingStrategy {

    public VictorAdvancedPointing(final int deckCount, final int[] cardValues) {
        super(deckCount, new double[] {0, 2, 2, 2, 3, 2, 2, 0, -1, -3, 0, 0, 0});
    }
}
