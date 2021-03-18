package commons.commands.invokers;

import commons.ICommand;
import commons.commands.ReDoPreviousUnDoCommand;
import commons.commands.ResetCommand;
import commons.commands.UnDoPreviousCommand;
import commons.commands.models.ResetState;

public interface ICommandInvoker extends IInvoker<ICommand> {

    void invoke(UnDoPreviousCommand command);

    void invoke(ReDoPreviousUnDoCommand command);

    void invoke(ResetCommand resetCommand);

    void undo();

    void redo();

    void reset();

    ResetState getResetState();

    void applyResetState(ResetState resetState);

    int getInvokedCount();

    ICommand getLastExecutedCommand();
}
