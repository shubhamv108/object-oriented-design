package pizzaordering;

public abstract class PizzaDecorator implements Pizza {

    protected Pizza pizza;

    protected PizzaDecorator(final Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public String getDescription() {
        return this.pizza.getDescription();
    }

    @Override
    public double getCost() {
        return this.pizza.getCost();
    }

    @Override
    public long getMakingTimeInSeconds() {
        return this.pizza.getMakingTimeInSeconds();
    }

    @Override
    public String toString() {
        return this.getDescription() + " : " + this.getCost();
    }
}
