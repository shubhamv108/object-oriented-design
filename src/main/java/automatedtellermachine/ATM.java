package automatedtellermachine;

import atmmachine.Bank;
import atmmachine.CardReader;
import atmmachine.CashDispenser;
import atmmachine.Keypad;
import automatedtellermachine.components.ICashDepositer;
import automatedtellermachine.components.IChequeDepositer;
import automatedtellermachine.components.screens.Screen;

public class ATM {

    String id;
    Address address;

    Screen screen;
    CardReader cardReader;
    Keypad keypad;
    CashDispenser cashDispenser;
    ICashDepositer cashDepositer;
    IChequeDepositer chequeDepositer;
    Bank bank;

}
