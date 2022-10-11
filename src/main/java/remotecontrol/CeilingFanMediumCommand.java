package remotecontrol;

public class CeilingFanMediumCommand implements Command {

    private final CeilingFan ceilingFan;
    int prevSpeed;

    public CeilingFanMediumCommand(final CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        prevSpeed = ceilingFan.getSpeed();
        ceilingFan.medium();
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
