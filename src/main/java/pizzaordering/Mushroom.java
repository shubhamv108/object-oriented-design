package pizzaordering;

public class Mushroom extends PizzaDecorator {
    protected Mushroom(final Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Mushroom";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.99;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
