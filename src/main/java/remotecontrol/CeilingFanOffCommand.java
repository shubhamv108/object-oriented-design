package remotecontrol;

public class CeilingFanOffCommand implements Command {

    private final CeilingFan ceilingFan;
    int prevSpeed;

    public CeilingFanOffCommand(final CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        prevSpeed = ceilingFan.getSpeed();
        ceilingFan.off();
    }

    @Override
    public void undo() {
        if (CeilingFan.HIGH == prevSpeed)
            ceilingFan.high();
        else if (CeilingFan.MEDIUM == prevSpeed)
            ceilingFan.medium();
        else if (CeilingFan.LOW == prevSpeed)
            ceilingFan.low();
        else if (CeilingFan.OFF == prevSpeed)
            ceilingFan.off();
    }
}
