package starbuzz.condiments;

import starbuzz.Beverage;
import starbuzz.Size;

import java.math.BigDecimal;

public class Soy extends CondimentDecorator {

    public Soy(final Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + ", Soy";
    }

    @Override
    public BigDecimal cost() {
        BigDecimal cost = this.beverage.cost();
        if (Size.TALL.equals(beverage.getSize()))
            cost.add(BigDecimal.valueOf(.10));
        else if (Size.VENTI.equals(beverage.getSize()))
            cost.add(BigDecimal.valueOf(.15));
        else if (Size.GRANDE.equals(beverage.getSize()))
            cost.add(BigDecimal.valueOf(.20));
        return cost;
    }
}
