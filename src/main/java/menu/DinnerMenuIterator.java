package menu;

import java.util.Iterator;

public class DinnerMenuIterator implements Iterator<MenuItem> {

    MenuItem[] menuItems;
    int position = 0;

    public DinnerMenuIterator(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean hasNext() {
        return position <= this.menuItems.length &&
               this.menuItems[position] != null;
    }

    @Override
    public MenuItem next() {
        return menuItems[position++];
    }
}
