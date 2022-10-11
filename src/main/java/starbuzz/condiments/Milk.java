package starbuzz.condiments;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class Milk extends CondimentDecorator {

    public Milk(final Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + ", Milk";
    }

    @Override
    public BigDecimal cost() {
        return this.beverage.cost().add(BigDecimal.valueOf(.40));
    }
}
