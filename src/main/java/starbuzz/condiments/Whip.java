package starbuzz.condiments;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class Whip extends CondimentDecorator {

    public Whip(final Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + ", Whip";
    }

    @Override
    public BigDecimal cost() {
        return this.beverage.cost().add(BigDecimal.valueOf(.10));
    }
}
