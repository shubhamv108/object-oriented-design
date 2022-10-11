package pizza;

public class PizzaTestDrive {
    public static void main(String[] args) {
        PizzaStore nyStore = new NYPizzaStore();// new NYStylePizzaStore();
        PizzaStore chicagoStore = new ChicagoPizzaStore(); // new ChicagoStylePizzaStore();

        Pizza pizza = nyStore.orderPizza("cheese");
        System.out.println("Ordered " + pizza.getName());

        pizza = chicagoStore.orderPizza("cheese");
        System.out.println("Ordered " + pizza.getName());
    }
}
