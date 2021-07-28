package commons.commands.invokers;

@FunctionalInterface
public interface IInvoker<Command> {

    void invoke(Command command);

}
