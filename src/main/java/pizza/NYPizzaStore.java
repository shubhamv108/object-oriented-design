package pizza;

public class NYPizzaStore extends PizzaStore {
    @Override
    public Pizza createPizza(String item) {
        Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();
        if ("cheese".equals(item)) {
            pizza = new CheesePizza(ingredientFactory);
            pizza.setName("NYStyleCheesePizza");
        } else if ("veggie".equals(item)) {
            pizza = new NYStyleVeggiePizza();
            pizza.setName("NYStyleVeggiePizza");
        } else if ("clam".equals(item)) {
            pizza = new NYStyleClamPizza();
            pizza.setName("NYStyleClamPizza");
        } else if ("pepperoni".equals(item)) {
            pizza = new NYStylePepperoniPizza();
            pizza.setName("NYStylePepperoniPizza");
        }
        return pizza;
    }
}
