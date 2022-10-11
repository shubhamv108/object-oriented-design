package starbuzz.coffee;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class Espresso extends Beverage {

    public Espresso() {
        description = "Espresso";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(1.99);
    }
}
