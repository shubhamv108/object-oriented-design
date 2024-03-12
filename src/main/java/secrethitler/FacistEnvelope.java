package secrethitler;

public class FacistEnvelope extends Envelope {

    @Override
    public boolean isValidCard(Card card) {
        return card instanceof Facist;
    }
}
