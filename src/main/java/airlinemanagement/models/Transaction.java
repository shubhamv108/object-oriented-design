package airlinemanagement.models;

public abstract class Transaction {

    private final String transactionReferenceNumber;

    private final Payment payment;

    protected Transaction(
            final String transactionReferenceNumber,
            final Payment payment) {
        this.transactionReferenceNumber = transactionReferenceNumber;
        this.payment = payment;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public boolean execute() {
        return true;
    }
}
