package starbuzz.condiments;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class Mocha extends CondimentDecorator {

    public Mocha(final Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + ", Mocha";
    }

    @Override
    public BigDecimal cost() {
        return this.beverage.cost().add(BigDecimal.valueOf(.20));
    }
}
