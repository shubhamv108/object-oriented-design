package atmmachine.cards;

import atmmachine.CardTransactionAuthenticationStatus;
import atmmachine.exceptions.CardBlockedForTransaction;
import atmmachine.exceptions.InvalidPINException;

import java.util.Date;

public abstract class AuthenticatedCard extends AbstractCard {

    private int pin;

    private boolean isPINResetRequired = true;

    private static final String DEFAULT_PIN = "";

    protected AuthenticatedCard(final String cardNumber, final String customerName, final Date expiryDate) {
        super(cardNumber, customerName, expiryDate);
    }

    private String getPIN() {
        return AuthenticatedCard.DEFAULT_PIN;
    }

    public boolean isPINResetRequired() {
        return this.isPINResetRequired;
    }

    public void authenticate(final String pin) {
        if (CardTransactionAuthenticationStatus.BLOCKED_FOR_24_HOURS.equals(this.getTransactionAuthenticationStatus())) {
            throw new CardBlockedForTransaction();
        }
        if (this.getPIN() != pin) {
//            throw new InvalidPINException(this.getTransactionAuthenticationStatus());
            throw new InvalidPINException(CardTransactionAuthenticationStatus.REMAINING_BEFORE_BLOCK_FOR_24_HOURS_2);
        }
    }

    private CardTransactionAuthenticationStatus getTransactionAuthenticationStatus() {
        return CardTransactionAuthenticationStatus.ACTIVE;
    }

}
