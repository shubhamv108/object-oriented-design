package atmmachine;

public enum CardTransactionAuthenticationStatus {

    ACTIVE("Active for transaction"),
    REMAINING_BEFORE_BLOCK_FOR_24_HOURS_2("2 attempts remianing before card gets blocked for 24 hours"),
    REMAINING_BEFORE_BLOCK_FOR_24_HOURS_1("1 attempts remianing before card gets blocked for 24 hours"),
    BLOCKED_FOR_24_HOURS("Blocked for 24 hours");

    private String value;

    CardTransactionAuthenticationStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
