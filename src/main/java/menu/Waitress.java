package menu;

import java.util.Iterator;
import java.lang.Iterable;

public class Waitress {

    MenuComponent allMenus;

    public Waitress(MenuComponent allMenus) {
        this.allMenus = allMenus;
    }

    public void printMenu() {
        allMenus.print();
    }

//    public void printMenu() {
//        Iterator<MenuItem> pancakeIterator = this.pancakeHouseMenu.iterator();
//        Iterator<MenuItem> dinnerIterator = this.dinnerMenu.iterator();
//
//        System.out.println("Breakfast");
//        printMenu(pancakeIterator);
//        System.out.println("Dinner");
//        printMenu(dinnerIterator);
//    }

    private void printMenu(Iterator<MenuItem> iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = iterator.next();
            System.out.println(menuItem.getName());
        }
    }

    private void printMenu(IMenu menu) {
        for (MenuItem item : menu)
            System.out.println(item.getName());
    }

    private void printMenu(Iterable<MenuItem> iterable) {
        for (MenuItem item : iterable)
            System.out.println(item.getName());
    }
}
