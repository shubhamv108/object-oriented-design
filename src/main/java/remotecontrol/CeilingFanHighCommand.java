package remotecontrol;

public class CeilingFanHighCommand implements Command {

    private final CeilingFan ceilingFan;
    int prevSpeed;

    public CeilingFanHighCommand(final CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        prevSpeed = ceilingFan.getSpeed();
        ceilingFan.high();
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
