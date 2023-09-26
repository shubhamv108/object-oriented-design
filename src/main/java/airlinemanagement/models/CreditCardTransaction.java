package airlinemanagement.models;

import java.util.Objects;
import java.util.UUID;

public class CreditCardTransaction extends Transaction {
    private String nameOnCard;
    private String cvv;
    private String expiry;

    public CreditCardTransaction(
            final String nameOnCard,
            final String cvv,
            final String expiry,
            final Payment payment) {
        super(UUID.randomUUID().toString(), payment);
        this.nameOnCard = nameOnCard;
        this.cvv = cvv;
        this.expiry = expiry;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final CreditCardTransaction that = (CreditCardTransaction) o;
        return Objects.equals(getTransactionReferenceNumber(), that.getTransactionReferenceNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTransactionReferenceNumber());
    }
}
