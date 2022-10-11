package remotecontrol;

public class StereoOnWithCDCCommand implements Command {
    protected Stereo stereo;

    public StereoOnWithCDCCommand(final Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        stereo.on();
        stereo.setCD();
        stereo.setVolume();
    }

//    @Override
    public void undo() {
        stereo.off();
    }
}
