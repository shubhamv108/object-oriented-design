package remotecontrol;

public class CeilingFanLowCommand implements Command {

    private final CeilingFan ceilingFan;
    int prevSpeed;

    public CeilingFanLowCommand(final CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        prevSpeed = ceilingFan.getSpeed();
        ceilingFan.low();
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
