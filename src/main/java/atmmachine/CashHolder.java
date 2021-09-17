package atmmachine;

import atmmachine.enums.cashbills.CurrencyBill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CashHolder {

    private final Map<CurrencyBill, Stack<CurrencyBill>> currencyBillsTotalCount = new HashMap<>();

    public void addCurrencyBill(final CurrencyBill currencyBill) {
        this.getCurrencyBillsTotalCount().getOrDefault(currencyBill, new Stack<>()).add(currencyBill);
    }

    private Map<CurrencyBill, Stack<CurrencyBill>> getCurrencyBillsTotalCount() {
        return this.currencyBillsTotalCount;
    }

    public List<CurrencyBill> getCash(final int amount, Class<CurrencyBill> currencyBillClass) {
        List<CurrencyBill> toBeDispensed = new ArrayList<>();
        CurrencyBill[] currencyBills = currencyBillClass.getEnumConstants();
        Arrays.sort(currencyBills, (x, y) -> y.getAmount() - x.getAmount());

        for (CurrencyBill currencyBill : currencyBills) {
            Stack<CurrencyBill> availableBills = this.getCurrencyBillsTotalCount().get(currencyBill);

            while ( availableBills.size() > 0 && ((Double) Math.floor(amount/currencyBill.getAmount())).intValue() > 0) {
                toBeDispensed.add(availableBills.pop());
            }
        }
        return toBeDispensed;
    }

    public boolean hasCash(final int amount, Class<CurrencyBill> currencyBillClass) {
        List<CurrencyBill> dispensible = new ArrayList<>();
        CurrencyBill[] currencyBills = currencyBillClass.getEnumConstants();
        Arrays.sort(currencyBills, (x, y) -> y.getAmount() - x.getAmount());

        for (CurrencyBill currencyBill : currencyBills) {
            Stack<CurrencyBill> availableBills = this.getCurrencyBillsTotalCount().get(currencyBill);

            while ( availableBills.size() > 0 && ((Double) Math.floor(amount/currencyBill.getAmount())).intValue() > 0) {
                dispensible.add(availableBills.pop());
            }
        }
        return amount == dispensible.stream().map(CurrencyBill::getAmount).reduce(0, (acc, amnt) -> acc += amnt);
    }


}
