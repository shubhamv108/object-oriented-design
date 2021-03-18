package commons.commands;

import commons.IBuilder;
import commons.ICommand;
import commons.commands.invokers.ICommandInvoker;

public class UnDoPreviousCommand implements ICommand {

    private ICommandInvoker commandInvoker;

    public UnDoPreviousCommand(final ICommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing UnDoPreviousCommand for %s", this.getCommandInvoker()));
        this.getCommandInvoker().undo();
        System.out.println(String.format("Executed UnDoPreviousCommand for %s", this.getCommandInvoker()));
    }

    @Override
    public void undo() {
        System.out.println(String.format("Undoing UnDoPreviousCommand command for %s", this.getCommandInvoker()));
        this.getCommandInvoker().redo();
        System.out.println(String.format("Undid UnDoPreviousCommand command for %s", this.getCommandInvoker()));
    }

    private ICommandInvoker getCommandInvoker() {
        return this.commandInvoker;
    }

    public static UnDoPreviousCommandBuilder builder() {
        return new UnDoPreviousCommandBuilder();
    }

    public static class UnDoPreviousCommandBuilder implements IBuilder<UnDoPreviousCommand> {
        private ICommandInvoker commandInvoker;

        public UnDoPreviousCommandBuilder withCommandInvoker(final ICommandInvoker commandInvoker) {
            this.commandInvoker = commandInvoker;
            return this;
        }

        @Override
        public UnDoPreviousCommand build() {
            return new UnDoPreviousCommand(this.commandInvoker);
        }
    }
}
