package pizzaordering;

public class PizzaOrderingSystem {
    public static void main(String[] args) {
        Pizza basic = new PlainPizza();
        Pizza withCheese = new Cheese(basic);

        System.out.println(basic);
        System.out.println(withCheese);
    }
}
