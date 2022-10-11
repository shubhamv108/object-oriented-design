package pizza;

public class ChicagoPizzaStore extends PizzaStore {
    @Override
    public Pizza createPizza(String item) {
        Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();
        if ("cheese".equals(item)) {
            pizza = new CheesePizza(ingredientFactory);
            pizza.setName("ChicagoStyleCheesePizza");
        } else if ("veggie".equals(item)) {
            pizza = new ChicagoStyleVeggiePizza();
            pizza.setName("ChicagoStyleVeggiePizza");
        } else if ("clam".equals(item)) {
            pizza = new ChicagoStylePepperoniPizza();
            pizza.setName("ChicagoStyleClamPizza");
        } else if ("pepperoni".equals(item)) {
            pizza = new ChicagoStylePepperoniPizza();
            pizza.setName("ChicagoStylePepperoniPizza");
        }
        return pizza;
    }
}
