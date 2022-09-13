package automatedtellermachine.bank.services;

import automatedtellermachine.Customer;
import automatedtellermachine.cards.CardInfo;
import automatedtellermachine.transactions.Transaction;
import automatedtellermachine.transactions.TransactionDetail;

public class BankAService implements IBankService {
    @Override
    public boolean isValidUser(CardInfo cardInfo, String pin) {
        return false;
    }

    @Override
    public Customer getCustomerDetails(CardInfo cardInfo) {
        return null;
    }

    @Override
    public TransactionDetail executeTransaction(Transaction transaction) {
        return null;
    }
}
