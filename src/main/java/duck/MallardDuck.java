package duck;

public class MallardDuck extends Duck {

    public MallardDuck() {
        this(new FlyWithWings(), new Quack());
    }

    public MallardDuck(FlyBehaviour flyBehaviour, QuackBehaviour quackBehaviour) {
        this.flyBehaviour = flyBehaviour;
        this.quackBehaviour = quackBehaviour;
    }

    @Override
    public void display() {
        System.out.println("MallardDuck");
    }
}
