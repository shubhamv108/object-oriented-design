package catalogsystem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddOn {
    private BigDecimal price;
    private final List<AddOnGroup> addOnGroups;

    public AddOn(final BigDecimal price, final AddOnGroup addOnGroup) {
        this.price = price;
        this.addOnGroups = new CopyOnWriteArrayList<>(Arrays.asList(addOnGroup));
    }

    public AddOn(final BigDecimal price, final List<AddOnGroup> addOnGroups) {
        this.price = price;
        this.addOnGroups = new CopyOnWriteArrayList<>(addOnGroups);
    }

}
