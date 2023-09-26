package airlinemanagement.models;

import java.util.Objects;
import java.util.UUID;

public class ChequeTransaction extends Transaction {
    private String bankName;
    private String chequeNumber;
    protected ChequeTransaction(
            final Payment payment,
            final String bankName,
            final String chequeNumber) {
        super(UUID.randomUUID().toString(), payment);
        this.bankName = bankName;
        this.chequeNumber = chequeNumber;
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
