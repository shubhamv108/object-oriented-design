package bowlingalley.strategies;

public interface IStrategy<Input, Output> {

    Output apply(Input input);

}
