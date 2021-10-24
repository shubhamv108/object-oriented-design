package automatedtellermachine.bank.services;


import automatedtellermachine.Customer;
import automatedtellermachine.cards.CardInfo;
import automatedtellermachine.transactions.Transaction;
import automatedtellermachine.transactions.TransactionDetail;

public interface IBankService {

    boolean isValidUser(CardInfo cardInfo, String pin);
    Customer getCustomerDetails(CardInfo cardInfo);
    TransactionDetail executeTransaction(Transaction transaction);

}
