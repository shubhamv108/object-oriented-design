package menu;

public class MenuTestDrive {
    public static void main(String[] args) {
//        PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
//        DinnerMenu dinnerMenu = new DinnerMenu();
//        CafeMenu cafeMenu = new CafeMenu();

        MenuComponent pancakeHouseMenu = new Menu("pancake");
        MenuComponent dinnerMenu = new Menu("dinner");
        MenuComponent cafeMenu = new Menu("cafe");
        MenuComponent dessertMenu = new Menu("dessert");

        MenuComponent allMenus = new Menu("All menus");
        allMenus.add(pancakeHouseMenu);
        allMenus.add(dinnerMenu);
        allMenus.add(cafeMenu);

        dinnerMenu.add(new MenuItem("Hotdog"));
        dinnerMenu.add(dessertMenu);
        dessertMenu.add(new MenuItem("ApplePie"));

        Waitress waitress = new Waitress(allMenus);
        waitress.printMenu();
    }
}
