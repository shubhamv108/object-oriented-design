package menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PancakeHouseMenuIterator implements Iterator<MenuItem> {

    List<MenuItem> menuItems;
    int position = 0;

    public PancakeHouseMenuIterator(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean hasNext() {
        return position <= this.menuItems.size() &&
               this.menuItems.get(position) != null;
    }

    @Override
    public MenuItem next() {
        return menuItems.get(position++);
    }
}
