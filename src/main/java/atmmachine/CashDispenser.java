package atmmachine;

import atmmachine.enums.cashbills.CurrencyBill;
import atmmachine.exceptions.NotEnoughCashToDispense;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CashDispenser {

    private final CashHolder cashHolder = new CashHolder();

    public List<CurrencyBill> dispenseCash(final int amount, Class<CurrencyBill> currencyBillClass) {
        if (this.canDispenseCash(amount, currencyBillClass)) {
            throw new NotEnoughCashToDispense();
        }
        return this.getCashHolder().getCash(amount, currencyBillClass);
    }

    public boolean canDispenseCash(final int amount, Class<CurrencyBill> currencyBillClass) {
        Method isAmountMultipleOfMethod = null;
        try {
            isAmountMultipleOfMethod = currencyBillClass.getMethod("isAmountInMultiplesOf", Double.class);
            isAmountMultipleOfMethod.invoke(null, amount);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return this.getCashHolder().hasCash(amount, currencyBillClass);
    }

    public CashHolder getCashHolder() {
        return this.cashHolder;
    }
}
