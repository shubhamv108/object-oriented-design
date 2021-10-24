package catalogsystem.variantitems;

import catalogsystem.AddOn;
import catalogsystem.Category;
import catalogsystem.Item;
import catalogsystem.variantitems.AbstractVariantItem;

import java.math.BigDecimal;
import java.util.Set;

public class MediumVariantItem extends AbstractVariantItem {
    public MediumVariantItem(Category category, Set<AddOn> availableAddOns, BigDecimal price, Item item) {
        super(category, availableAddOns, price, item);
    }
}
