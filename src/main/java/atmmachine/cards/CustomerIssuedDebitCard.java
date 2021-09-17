package atmmachine.cards;

import java.util.Date;

public class CustomerIssuedDebitCard extends AbstractCard {
    protected CustomerIssuedDebitCard(String cardNumber, String customerName, Date expiryDate) {
        super(cardNumber, customerName, expiryDate);
    }
}
