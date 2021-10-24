package catalogsystem.variantitems;

import catalogsystem.AddOn;
import catalogsystem.Category;
import catalogsystem.Item;

import java.math.BigDecimal;
import java.util.Set;

public class SmallVariantItem extends AbstractVariantItem {
    public SmallVariantItem(Category category, Set<AddOn> availableAddOns, BigDecimal price, Item item) {
        super(category, availableAddOns, price, item);
    }
}
