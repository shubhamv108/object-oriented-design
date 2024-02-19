package pizzaordering;

public class Pepperoni extends PizzaDecorator {
    protected Pepperoni(final Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Pepperoni";
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.49;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
