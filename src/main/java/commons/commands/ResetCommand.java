package commons.commands;

import commons.IBuilder;
import commons.ICommand;
import commons.commands.invokers.ICommandInvoker;
import commons.commands.models.ResetState;

public class ResetCommand implements ICommand {

    private ResetState resetState;
    private final ICommandInvoker commandInvoker;

    protected ResetCommand(final ICommandInvoker commandInvoker, final ResetState resetState) {
        this.commandInvoker = commandInvoker;
        this.resetState = resetState;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing reset command for %s with state %s", this.getCommandInvoker(), this.getResetState()));
        this.setResetState(this.getResetState());
        this.getCommandInvoker().reset();
        this.executeReset();
        System.out.println(String.format("Executed reset command for %s with state %s", this.getCommandInvoker(), this.getResetState()));
    }

    protected void executeReset() {};

    @Override
    public void undo() {
        System.out.println(String.format("UnDoing reset command for %s with state %s", this.getCommandInvoker(), this.getResetState()));
        this.executeReset();
        this.getCommandInvoker().applyResetState(this.getResetState());
        System.out.println(String.format("UnDid reset command for %s with state %s", this.getCommandInvoker(), this.getResetState()));

    }

    public void setResetState(final ResetState resetState) {
        this.resetState = resetState;
    }

    public ResetState getResetState() {
        return this.resetState;
    }

    private ICommandInvoker getCommandInvoker() {
        return this.commandInvoker;
    }

    public static ResetCommandBuilder builder() {
        return new ResetCommandBuilder();
    }

    public static class ResetCommandBuilder implements IBuilder<ResetCommand> {
        private ResetState resetState;
        private ICommandInvoker commandInvoker;

        public ResetCommandBuilder withCommandInvoker(final ICommandInvoker commandInvoker) {
            this.commandInvoker = commandInvoker;
            this.resetState = commandInvoker.getResetState();
            return this;
        }

        @Override
        public ResetCommand build() {
            return new ResetCommand(this.getCommandInvoker(), this.resetState);
        }

        protected ICommandInvoker getCommandInvoker() {
            return commandInvoker;
        }

        protected ResetState getResetState() {
            return this.resetState;
        }
    }
}
