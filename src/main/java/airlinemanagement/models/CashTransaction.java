package airlinemanagement.models;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CashTransaction extends Transaction {

    private final BigDecimal cashTendered;
    protected CashTransaction(
            final BigDecimal cashTendered,
            final Payment payment) {
        super(UUID.randomUUID().toString(), payment);
        this.cashTendered = cashTendered;
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
