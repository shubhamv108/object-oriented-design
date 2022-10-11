package pizza;

import java.util.ArrayList;
import java.util.List;

public abstract class Pizza {
    protected String name;

    protected Dough dough;
    protected Sauce sauce;
    protected Veggie[] veggies;
    protected Cheese cheese;
    protected Pepperoni pepperoni;
    protected Clam clam;

    protected final List<String> toppings = new ArrayList<>();
    public abstract void prepare();
    public void bake() {
        System.out.println("Baking");
    }
    public void cut() {
        System.out.println("Cutting");
    }
    public void box() {
        System.out.println("Boxing");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
