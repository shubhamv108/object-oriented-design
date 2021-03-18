package commons.commands;

import commons.IBuilder;
import commons.ICommand;
import commons.commands.invokers.ICommandInvoker;
import commons.commands.invokers.IInvoker;

public class ReDoPreviousUnDoCommand implements ICommand {
    private ICommandInvoker commandInvoker;

    public ReDoPreviousUnDoCommand(final ICommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing UnDoPreviousCommand for %s", this.getCommandInvoker()));
        this.getCommandInvoker().redo();
        System.out.println(String.format("Executed UnDoPreviousCommand for %s", this.getCommandInvoker()));
    }

    @Override
    public void undo() {
        System.out.println(String.format("Undoing UnDoPreviousCommand command for %s", this.getCommandInvoker()));
        this.getCommandInvoker().undo();
        System.out.println(String.format("Undid UnDoPreviousCommand command for %s", this.getCommandInvoker()));
    }

    private ICommandInvoker getCommandInvoker() {
        return this.commandInvoker;
    }

    public static ReDoPreviousUnDoCommandBuilder builder() {
        return new ReDoPreviousUnDoCommandBuilder();
    }

    public static class ReDoPreviousUnDoCommandBuilder implements IBuilder<ReDoPreviousUnDoCommand> {
        private ICommandInvoker commandInvoker;

        public ReDoPreviousUnDoCommandBuilder withCommandInvoker(final ICommandInvoker commandInvoker) {
            this.commandInvoker = commandInvoker;
            return this;
        }

        @Override
        public ReDoPreviousUnDoCommand build() {
            return new ReDoPreviousUnDoCommand(this.commandInvoker);
        }
    }
}
