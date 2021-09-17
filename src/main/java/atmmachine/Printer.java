package atmmachine;

public class Printer {

    public boolean printReceipt(Transaction transaction) {
        System.out.println(transaction);
        return true;
    }

}
