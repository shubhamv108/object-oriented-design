package commons;

@FunctionalInterface
public interface IFactory<Input, Product> {

    Product get(Input input);

}
