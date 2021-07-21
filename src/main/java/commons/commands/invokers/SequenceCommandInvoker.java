package commons.commands.invokers;

import commons.builder.IBuilder;
import commons.ICommand;
import commons.commands.ReDoPreviousUnDoCommand;
import commons.commands.ResetCommand;
import commons.commands.UnDoPreviousCommand;
import commons.commands.models.ResetState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SequenceCommandInvoker implements ICommandInvoker {

    private final List<ResetCommand> resetCommands = new ArrayList<>();
    private final List<ICommand> commands = new ArrayList<>();
    private int currentSequenceNumber = -1;

    @Override
    public void invoke(final ICommand command) {
        this.setCommand(command);
    }

    private void setCommand(final ICommand command) {
        IntStream.iterate(this.getCommands().size() - 1, i -> i > this.getCurrentSequenceNumber(), i -> i - 1)
                .forEach(index -> this.getCommands().remove(index));
        this.getCommands().add(command);
        this.incrementSequenceNumber();
        try {
            command.execute();
        } catch (final Exception ex) {
            this.getCommands().remove(this.getCurrentSequenceNumber());
            this.decrementSequenceNumber();
            throw ex;
        }
    }

    @Override
    public void invoke(final UnDoPreviousCommand command) {
        command.execute();
    }

    @Override
    public void undo() {
        if (!this.getCommands().isEmpty()) {
            ICommand commandToUndo = this.getCommands().get(this.getCurrentSequenceNumber());
            this.decrementSequenceNumber();
            try {
                commandToUndo.undo();
            } catch (final Exception ex) {
                this.incrementSequenceNumber();
                throw ex;
            }
        }
    }

    @Override
    public void invoke(final ReDoPreviousUnDoCommand command) {
        command.execute();
    }

    @Override
    public void redo() {
        if (this.getCurrentSequenceNumber() < this.getCommands().size() - 1) {
            this.incrementSequenceNumber();
            ICommand commandToRedo = this.getCommands().get(this.getCurrentSequenceNumber());
            commandToRedo.execute();
        }
    }

    @Override
    public void invoke(final ResetCommand resetCommand) {
        this.setCommand(resetCommand);
    }

    protected void setCommand(final ResetCommand resetCommand) {
        this.resetCommands.add(resetCommand);
        resetCommand.execute();
    }

    @Override
    public void reset() {
        this.getCommands().clear();
        this.currentSequenceNumber = -1;
    }

    @Override
    public ResetState getResetState() {
        return ResetState.builder()
                .withCommands((ArrayList<ICommand>) this.getCommands())
                .withCurrentSequenceMember(this.getCurrentSequenceNumber())
                .build();
    }

    private void incrementSequenceNumber() {
        this.currentSequenceNumber++;
    }

    private void decrementSequenceNumber() {
        this.currentSequenceNumber--;
    }

    @Override
    public int getInvokedCount() {
        return this.getCurrentSequenceNumber() + 1;
    }

    @Override
    public void applyResetState(final ResetState resetState) {
        this.getCommands().clear();
        int executeCommandCount = resetState.getCurrentSequenceNumber();
        for (int index = 0; index <= executeCommandCount; index++) {
            ICommand command = resetState.getCommands().get(index);
            this.setCommand(command);
        }
        for (int index = executeCommandCount + 1; index < resetState.getCommands().size(); index++) {
            ICommand command = resetState.getCommands().get(index);
            this.getCommands().add(command);
        }
    }

    @Override
    public ICommand getLastExecutedCommand() {
        return this.getCommands().get(this.getCurrentSequenceNumber());
    }

    private int getCurrentSequenceNumber() {
        return this.currentSequenceNumber;
    }

    private List<ICommand> getCommands() {
        return this.commands;
    }

    public static SequenceCommandInvokerBuilder builder() {
        return new SequenceCommandInvokerBuilder();
    }

    public static class SequenceCommandInvokerBuilder implements IBuilder<SequenceCommandInvoker> {
        @Override
        public SequenceCommandInvoker build() {
            return new SequenceCommandInvoker();
        }
    }

}
