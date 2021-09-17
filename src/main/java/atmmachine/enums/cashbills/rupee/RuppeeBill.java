package atmmachine.enums.cashbills.rupee;

import atmmachine.enums.cashbills.CurrencyBill;
import atmmachine.exceptions.InvalidAmountNotInMultipleOf;

public class RuppeeBill extends CurrencyBill {

    public static final String SIGN = "₹";

    protected RuppeeBill(final Integer amount) {
        super(amount);
    }

    protected static final Boolean isAmountInMultiplesOf(final Double amount) {
        if (amount % 100 != 0) throw new InvalidAmountNotInMultipleOf(SIGN, 100);
        return true;
    }

}
