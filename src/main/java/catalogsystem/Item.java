package catalogsystem;

import catalogsystem.variantitems.AbstractVariantItem;

import java.util.Set;

public class Item {
    private String name;
    private Set<AbstractVariantItem> variantItems;

    public Item(final String name, final Set<AbstractVariantItem> variantItems) {
        this.name = name;
        this.variantItems = variantItems;
    }
}
