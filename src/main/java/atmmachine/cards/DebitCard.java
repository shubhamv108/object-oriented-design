package atmmachine.cards;

import java.util.Date;

public class DebitCard extends AuthenticatedCard {
    protected DebitCard(final String cardNumber, final String customerName, final Date expiryDate) {
        super(cardNumber, customerName, expiryDate);
    }
}
