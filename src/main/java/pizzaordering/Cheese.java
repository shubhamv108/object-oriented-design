package pizzaordering;

public class Cheese extends PizzaDecorator {
    protected Cheese(final Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Cheese";
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.99;
    }

    @Override
    public long getMakingTimeInSeconds() {
        return super.getMakingTimeInSeconds() + 10;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
