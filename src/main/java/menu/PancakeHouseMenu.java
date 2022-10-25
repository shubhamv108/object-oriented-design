package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PancakeHouseMenu implements IMenu {

    List<MenuItem> menuItems;

    public PancakeHouseMenu() {
        menuItems = new ArrayList<>();
        addItem("Waffles");
    }

    private void addItem(String name) {
        menuItems.add(new MenuItem(name));
    }

    @Override
    public Iterator<MenuItem> iterator() {
        return this.menuItems.iterator();
    }
}
