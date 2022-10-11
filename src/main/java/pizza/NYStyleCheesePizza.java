package pizza;

public class NYStyleCheesePizza extends Pizza {
    public NYStyleCheesePizza() {
        name = "NYStyleCheesePizza";
//        dough = "Thin crust dough";
//        sauce = "Marian Sauce";

        toppings.add("Grated Raggiano Cheese");
    }

    @Override
    public void prepare() {

    }
}
