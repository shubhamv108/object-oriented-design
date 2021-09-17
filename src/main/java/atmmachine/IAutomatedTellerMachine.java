package atmmachine;

import atmmachine.accounts.Account;
import atmmachine.cards.AuthenticatedCard;
import atmmachine.enums.ATMMachineOperation;
import atmmachine.enums.cashbills.CurrencyBill;
import java.util.List;

public interface IAutomatedTellerMachine {
    void insertCurrencyBill(CurrencyBill bill);
    default void insertCurrencyBill(List<CurrencyBill> bills) {
        bills.forEach(bill -> insertCurrencyBill(bill));
    }
    List<CurrencyBill> removeCurrencyBill();

    void insertCard(AuthenticatedCard card);
    void enterPIN(String pin);
    Account selectAccountType();
    ATMMachineOperation selectOperation();
}
