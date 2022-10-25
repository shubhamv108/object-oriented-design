package menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CafeMenu implements IMenu {
    Map<String, MenuItem> menuItems = new HashMap<>();

    public CafeMenu() {
        addItem("Burrito");
    }

    public void addItem(String name) {
        MenuItem menuItem = new MenuItem(name);
        menuItems.put(name, menuItem);
    }

    @Override
    public Iterator<MenuItem> iterator() {
        return this.menuItems.values().iterator();
    }
}
