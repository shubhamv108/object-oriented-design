package starbuzz.condiments;

import starbuzz.Beverage;
import starbuzz.Size;

import java.math.BigDecimal;

public abstract class CondimentDecorator extends Beverage {

    protected final Beverage beverage;

    public CondimentDecorator(final Beverage beverage) {
        this.beverage = beverage;
    }

    public abstract String getDescription();

    @Override
    public Size getSize() {
        return super.getSize();
    }
}
