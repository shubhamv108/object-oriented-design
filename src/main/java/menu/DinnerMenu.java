package menu;

import java.util.Iterator;

public class DinnerMenu implements IMenu {

    static final int MAX_ITEMS = 6;
    int numberOfItems = 0;
    MenuItem[] menuItems;

    public DinnerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];
        addItem("Hotdog");
    }

    private void addItem(String name) {
        if (numberOfItems == MAX_ITEMS)
            System.out.println("IMenu is full!");
        menuItems[numberOfItems++] = new MenuItem(name);
    }


    @Override
    public Iterator<MenuItem> iterator() {
        return new DinnerMenuIterator(menuItems);
    }
}
