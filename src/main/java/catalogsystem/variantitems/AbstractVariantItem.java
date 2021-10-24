package catalogsystem.variantitems;

import catalogsystem.AddOn;
import catalogsystem.Category;
import catalogsystem.Item;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractVariantItem {
    private Category category;
    private Set<AddOn> availableAddOns;
    private BigDecimal price;
    private Item item;

    public AbstractVariantItem(final Category category, final Set<AddOn> availableAddOns, final BigDecimal price, final Item item) {
        this.category = category;
        this.availableAddOns = new HashSet<>(availableAddOns);
        this.price = price;
        this.item = item;
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
