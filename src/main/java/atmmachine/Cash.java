package atmmachine;

import atmmachine.enums.cashbills.CurrencyBill;

import java.util.ArrayList;
import java.util.List;

public class Cash {
    private List<CurrencyBill> currencyBills = new ArrayList<>();

    public List<CurrencyBill> getCurrencyBills() {
        return this.currencyBills;
    }

    public List<CurrencyBill> getValidBills() {
        return this.getCurrencyBills();
    }

    public List<CurrencyBill> getInvalidBills() {
        return this.getCurrencyBills();
    }
}
