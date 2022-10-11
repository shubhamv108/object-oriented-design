package starbuzz.coffee;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class Decaf extends Beverage {

    public Decaf() {
        description = "Decaf";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(0.65);
    }
}
