package bowlingalley.factories;

public interface IFactory<Input, Product> {

    Product get(Input input);

}
