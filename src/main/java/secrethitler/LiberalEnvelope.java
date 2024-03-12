package secrethitler;

public class LiberalEnvelope extends Envelope {
    @Override
    public boolean isValidCard(Card card) {
        return card instanceof Liberal;
    }
}
