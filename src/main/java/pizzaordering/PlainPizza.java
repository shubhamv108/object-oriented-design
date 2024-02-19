package pizzaordering;

public class PlainPizza implements Pizza {

    @Override
    public String getDescription() {
        return "PlainPizza";
    }

    @Override
    public double getCost() {
        return 5.99;
    }

    @Override
    public long getMakingTimeInSeconds() {
        return 300;
    }


    @Override
    public String toString() {
        return this.getDescription() + " : " + this.getCost();
    }
}
