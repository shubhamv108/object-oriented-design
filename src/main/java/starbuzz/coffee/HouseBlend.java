package starbuzz.coffee;

import starbuzz.Beverage;

import java.math.BigDecimal;

public class HouseBlend extends Beverage {

    public HouseBlend() {
        description = "HouseBlend";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(0.89);
    }
}
