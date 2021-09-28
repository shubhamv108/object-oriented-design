package automatedtellermachine.components;

import automatedtellermachine.cash.Cash;

import java.util.List;

public interface ICashDepositer {

    public boolean acceptCash(List<Cash> cashNotes, double amount);

}
