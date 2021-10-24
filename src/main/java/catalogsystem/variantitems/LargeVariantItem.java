package catalogsystem.variantitems;

import catalogsystem.AddOn;
import catalogsystem.Category;
import catalogsystem.Item;
import catalogsystem.variantitems.AbstractVariantItem;

import java.math.BigDecimal;
import java.util.Set;

public class LargeVariantItem extends AbstractVariantItem {
    public LargeVariantItem(Category category, Set<AddOn> availableAddOns, BigDecimal price, Item item) {
        super(category, availableAddOns, price, item);
    }
}
