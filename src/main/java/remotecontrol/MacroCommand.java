package remotecontrol;

import java.util.stream.IntStream;

public class MacroCommand implements Command {
    Command[] commands;

    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        IntStream.range(0, commands.length).
                forEach(i -> commands[i].execute());
    }

    @Override
    public void undo() {
        IntStream.range(0, commands.length).
                forEach(i -> commands[i].undo());
    }
}
