package atmmachine.cards;

import java.util.Date;

public interface Card {
    String getCardNumber();

    String getCustomerName();

    Date getExpiryDate();
}
