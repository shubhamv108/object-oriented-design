package vendingmachine;

import vendingmachine.admin.IAdmin;
import vendingmachine.admin.impl.Admin;
import vendingmachine.enums.Button;
import vendingmachine.enums.Coin;
import vendingmachine.impl.VendingMachine;
import vendingmachine.models.Item;

public class Test {
    public static void main(String[] args) {
        IVendingMachine machine = new VendingMachine(5, 10);

        IAdmin admin1 = new Admin("1");
        admin1.administer(machine);

        IAdmin admin2 = new Admin("2");
        admin2.administer(machine);

        Item item = new Item("1", "item1");

//        admin1.administer(machine);
        admin1.refill(machine, Button.ONE, item);
        machine.insertCoin(Coin.FIVE);
//        machine.insertCoin(Coin.ONE);

//        admin1.unsubscribe(machine);

        Item dispendsedItem = machine.pressButton(Button.ONE);

        if (dispendsedItem == item) {
            System.out.println("Correct item dispensed");
        }

//        admin1.remove(machine, item);
    }
}
