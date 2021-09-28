package automatedtellermachine.bank.services;


import automatedtellermachine.cards.CardInfo;

public interface IBankService {

    boolean isValidUser(CardInfo cardInfo, String pin);
    Customer getCustomerDetails(CardInfo cardInfo);
    TransactionDetail executeTransaction(Transaction transaction);

}
