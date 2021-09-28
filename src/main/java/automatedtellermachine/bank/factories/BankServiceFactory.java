package automatedtellermachine.bank.factories;

import automatedtellermachine.bank.services.IBankService;
import automatedtellermachine.cards.CardInfo;

public interface BankServiceFactory {

    IBankService getBankService(CardInfo cardInfo);

}
