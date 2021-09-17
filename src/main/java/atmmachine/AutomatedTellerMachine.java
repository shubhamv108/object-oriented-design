package atmmachine;

import atmmachine.accounts.Account;
import atmmachine.cards.AuthenticatedCard;
import atmmachine.enums.ATMMachineOperation;
import atmmachine.enums.cashbills.CurrencyBill;
import java.util.List;

public class AutomatedTellerMachine implements IAutomatedTellerMachine {



    @Override
    public void insertCurrencyBill(CurrencyBill bill) {

    }

    @Override
    public List<CurrencyBill> removeCurrencyBill() {
        return null;
    }

    @Override
    public void insertCard(AuthenticatedCard card) {

    }

    @Override
    public void enterPIN(String pin) {

    }

    @Override
    public Account selectAccountType() {
        return null;
    }

    @Override
    public ATMMachineOperation selectOperation() {
        return null;
    }
}
