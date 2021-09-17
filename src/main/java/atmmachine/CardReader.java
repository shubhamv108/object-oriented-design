package atmmachine;

import atmmachine.accounts.Account;
import atmmachine.cards.CustomerIssuedDebitCard;

public class CardReader {

    public boolean readCard(final CustomerIssuedDebitCard card) {
        Account account = Banks.getInstance().getAccount(card.getCardNumber());
        return true;
    }
}
