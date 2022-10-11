package remotecontrol;

import java.util.stream.IntStream;

public class RemoteControlWithUndo {
    protected Command[] onCommands;
    protected Command[] offCommands;
    protected Command undoCommand;

    public RemoteControlWithUndo() {
        onCommands = new Command[7];
        offCommands = new Command[7];
        IntStream.range(0, 7).
                forEach(i -> {
                    onCommands[i] = new NoCommand();
                    offCommands[i] = new NoCommand(); });
        undoCommand = new NoCommand();
    }

    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonWasPushed(int slot) {
        onCommands[slot].execute();
        undoCommand = onCommands[slot];
    }

    public void offButtonWasPushed(int slot) {
        offCommands[slot].execute();
        undoCommand = offCommands[slot];
    }

    public void undoButtonWasPushed() {
        undoCommand.undo();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, 7).
                mapToObj(i ->
                        String.format("%s %s %s",
                                i,
                                onCommands[i].getClass().getName(),
                                offCommands[i].getClass().getName())).
                map(String::valueOf).
                forEach(e -> builder.append(e).append("\n"));
        return builder.toString();
    }
}
