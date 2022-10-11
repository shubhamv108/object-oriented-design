package duck;

public class ModelDuck extends Duck {

    public ModelDuck() {
        this(new FlyNoWay(), new Quack());
    }

    public ModelDuck(FlyBehaviour flyBehaviour, QuackBehaviour quackBehaviour) {
        this.flyBehaviour = flyBehaviour;
        this.quackBehaviour = quackBehaviour;
    }

    @Override
    public void display() {
        System.out.println("ModelDuck");
    }
}
