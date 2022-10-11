package pizza;

public class CaliforniaStylePizzaStore extends PizzaStore {
    @Override
    public Pizza createPizza(String item) {
        if ("cheese".equals(item))
            return new CaliforniaStyleCheesePizza();
        else if ("veggie".equals(item))
            return new CaliforniaStyleVeggiePizza();
        else if ("clam".equals(item))
            return new CaliforniaStyleClamPizza();
        else if ("pepperoni".equals(item))
            return new CaliforniaStylePepperoniPizza();
        return null;
    }
}
