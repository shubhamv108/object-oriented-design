package atmmachine.cards;

import commons.builder.IBuilder;

import java.util.Date;
import java.util.Random;

public abstract class AbstractCard implements Card {

    private final String cardNumber;
    private final String customerName;
    private final Date expiryDate;

    protected AbstractCard(String cardNumber, String customerName, Date expiryDate) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.expiryDate = expiryDate;
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    public static abstract class AbstractCardBuilder<Card, CardBuilder> implements IBuilder<Card> {

        private String cardNumber;
        private String customerName;
        private Date expiryDate;

        private static final Random random = new Random();

        public CardBuilder withCardNumber(final String cardNumber) {
            this.cardNumber = cardNumber;
            return (CardBuilder) this;
        }

        public CardBuilder withCustomerName(final String customerName) {
            this.customerName = customerName;
            return (CardBuilder) this;
        }

        public CardBuilder withExpiryDate(final Date expiryDate) {
            this.expiryDate = expiryDate;
            return (CardBuilder) this;
        }
    }

}
