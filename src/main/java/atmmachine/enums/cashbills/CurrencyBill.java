package atmmachine.enums.cashbills;

public abstract class CurrencyBill {
    private final Integer amount;

    protected CurrencyBill(final Integer amount) {
        this.amount = amount;
    }

    protected static Boolean isAmountInMultiplesOf(final Double amount) {
        return true;
    }

    public final int getAmount() {
        return this.amount;
    }
}