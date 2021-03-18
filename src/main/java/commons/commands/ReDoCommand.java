package commons.commands;

import commons.IBuilder;
import commons.ICommand;

public class ReDoCommand implements ICommand {

    private final UnDoCommand unDoCommand;

    private ReDoCommand(final UnDoCommand unDoCommand) {
        this.unDoCommand = unDoCommand;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Executing undo unDoCommand for %s", this.getUnDoCommand()));
        this.getUnDoCommand().undo();
        System.out.println(String.format("Executed undo unDoCommand for %s", this.getUnDoCommand()));
    }

    @Override
    public void undo() {
        System.out.println(String.format("Undoing undo unDoCommand for %s", this.getUnDoCommand()));
        this.getUnDoCommand().execute();
        System.out.println(String.format("Undid undo unDoCommand for %s", this.getUnDoCommand()));
    }

    public ICommand getUnDoCommand() {
        return this.unDoCommand;
    }

    public static ReDoCommandBuilder builder() {
        return new ReDoCommandBuilder();
    }

    public static class ReDoCommandBuilder implements IBuilder<ReDoCommand> {
        private UnDoCommand command;

        public ReDoCommandBuilder withUnDoCommand(final UnDoCommand command) {
            this.command = command;
            return this;
        }

        @Override
        public ReDoCommand build() {
            return new ReDoCommand(this.command);
        }
    }
}
