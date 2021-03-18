package commons.commands.invokers;

import commons.commands.ResetCommand;

@FunctionalInterface
public interface IInvoker<Command> {

    void invoke(Command command);

}
