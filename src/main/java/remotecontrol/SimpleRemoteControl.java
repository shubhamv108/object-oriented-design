package remotecontrol;

public class SimpleRemoteControl {
    protected Command slot;
    public SimpleRemoteControl() {}

    public void setCommand(Command command) {
        slot = command;
    }

    public void buttonWasReleased() {
        slot.execute();
    }
}
