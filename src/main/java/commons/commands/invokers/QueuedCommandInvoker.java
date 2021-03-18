package commons.commands.invokers;

import commons.ICommand;
import commons.commands.ReDoCommand;
import commons.commands.ReDoPreviousUnDoCommand;
import commons.commands.ResetCommand;
import commons.commands.UnDoPreviousCommand;
import commons.commands.models.ResetState;
import commons.commands.UnDoCommand;

import java.util.LinkedList;
import java.util.Queue;

public class QueuedCommandInvoker implements ICommandInvoker {

    private final Queue<ICommand> commands = new LinkedList<>();
    private final Queue<UnDoCommand> unDoCommands = new LinkedList<>();
    private final Queue<ResetCommand> resetCommands = new LinkedList<>();

    @Override
    public void invoke(final ICommand command) {
        this.setCommand(command);
    }

    protected void setCommand(final ICommand command) {
        if (!this.getUnDoCommands().isEmpty()) {
            this.getUnDoCommands().clear();
        }
        this.getCommands().add(command);
        command.execute();
    }

    @Override
    public void invoke(final UnDoPreviousCommand command) {
        command.execute();
    }

    @Override
    public void invoke(final ReDoPreviousUnDoCommand command) {
        command.execute();
    }

    protected void setCommand(final ReDoCommand command) {
        this.getCommands().add(command);
        command.execute();
    }

    @Override
    public void undo() {
        if (!this.getCommands().isEmpty()) {
            ICommand command = this.getCommands().poll();
            UnDoCommand unDoCommand = UnDoCommand.builder().withCommand(command).build();
            this.getUnDoCommands().offer(unDoCommand);
            unDoCommand.execute();
        }
    }

    @Override
    public void redo() {
        if (!this.getUnDoCommands().isEmpty()) {
            UnDoCommand unDoCommand = this.getUnDoCommands().poll();
            ReDoCommand reDoCommand = ReDoCommand.builder().withUnDoCommand(unDoCommand).build();
            this.setCommand(reDoCommand);
        }
    }

    @Override
    public void reset() {
        this.getCommands().clear();
        this.getUnDoCommands().clear();
    }

    @Override
    public void invoke(final ResetCommand resetCommand) {
        this.setCommand(resetCommand);
    }

    protected void setCommand(final ResetCommand resetCommand) {
        this.getResetCommands().add(resetCommand);
        resetCommand.execute();
    }


    @Override
    public void applyResetState(final ResetState resetState) {
        this.reset();
        resetState.getCommands().forEach(command -> this.setCommand(command));
        resetState.getUnDoCommands().forEach(command -> this.getUnDoCommands().add(command));
    }

    @Override
    public ICommand getLastExecutedCommand() {
        return this.getCommands().peek();
    }

    @Override
    public ResetState getResetState() {
        return ResetState.builder()
                .withCommands((LinkedList) this.getCommands())
                .withUnDoCommands((LinkedList) this.getUnDoCommands())
                .build();
    }

    @Override
    public int getInvokedCount() {
        return this.getCommands().size();
    }

    public void unDoInReverseOrder() {
        while (!this.getCommands().isEmpty()) {
            this.getCommands().poll().undo();
        }
    }

    private Queue<ICommand> getCommands() {
        return this.commands;
    }

    private Queue<UnDoCommand> getUnDoCommands() {
        return this.unDoCommands;
    }

    private Queue<ResetCommand> getResetCommands() {
        return this.resetCommands;
    }
}
