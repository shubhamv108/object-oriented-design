package pizza;

public class ChicagoStyleCheesePizza extends Pizza {
    public ChicagoStyleCheesePizza() {
        name = "ChicagoStyleCheesePizza";

        toppings.add("Shredded Mozzarella Cheese");
    }

    @Override
    public void prepare() {

    }

    @Override
    public void cut() {
        System.out.println("Cutting into square slices");
    }
}
