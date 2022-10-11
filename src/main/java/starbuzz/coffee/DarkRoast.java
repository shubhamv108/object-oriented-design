package starbuzz.coffee;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class DarkRoast extends Beverage {

    public DarkRoast() {
        description = "DarkRoast";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(0.99);
    }
}
