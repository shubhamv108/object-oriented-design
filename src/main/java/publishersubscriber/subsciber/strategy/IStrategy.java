package publishersubscriber.subsciber.strategy;

public interface IStrategy<Input, Output> {

    Output apply(Input input);

}
