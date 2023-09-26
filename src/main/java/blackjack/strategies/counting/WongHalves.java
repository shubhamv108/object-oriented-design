package blackjack.strategies.counting;

public class WongHalves extends CountingStrategy {

    public WongHalves(final int deckCount, final int[] cardValues) {
        super(deckCount, new double[] {-1, 0.5, 1, 1, 1.5, 1, 0.5, 0, -0.5, -1, -1, -1, -1});
    }
}
