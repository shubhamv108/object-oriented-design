package commons.commands;

import commons.builder.IBuilder;
import commons.ICommand;

public class UnDoCommand implements ICommand {

    private final ICommand command;

    public UnDoCommand(final ICommand command) {
        this.command = command;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing undo command for %s", this.getCommand()));
        this.getCommand().undo();
        System.out.println(String.format("Executed undo command for %s", this.getCommand()));
    }

    @Override
    public void undo() {
        System.out.println(String.format("Undoing undo command for %s", this.getCommand()));
        this.getCommand().execute();
        System.out.println(String.format("Undid undo command for %s", this.getCommand()));
    }

    public ICommand getCommand() {
        return this.command;
    }

    public static UnDoCommandBuilder builder() {
        return new UnDoCommandBuilder();
    }

    public static class UnDoCommandBuilder implements IBuilder<UnDoCommand> {
        private ICommand command;

        public UnDoCommandBuilder withCommand(final ICommand command) {
            this.command = command;
            return this;
        }

        @Override
        public UnDoCommand build() {
            return new UnDoCommand(this.command);
        }
    }
}
