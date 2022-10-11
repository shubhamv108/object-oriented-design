package starbuzz;

import java.math.BigDecimal;

public abstract class Beverage {
    protected Size size = Size.TALL;
    protected String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public abstract BigDecimal cost();

    @Override
    public String toString() {
        return this.getDescription() + ", $" + this.cost();
    }
}
