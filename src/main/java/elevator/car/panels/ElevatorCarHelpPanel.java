package elevator.car.panels;

import elevator.car.buttons.ElevatorCarHelpCallButton;
import elevator.car.components.ElevatorCarMicrophone;
import elevator.car.components.ElevatorCarSpeaker;

public class ElevatorCarHelpPanel {
    private ElevatorCarMicrophone microphone;
    private ElevatorCarSpeaker speaker;
    private ElevatorCarHelpCallButton callButton;
    private ElevatorCarPanel carPanel;

    public ElevatorCarHelpPanel(final ElevatorCarPanel carPanel) {
        this.callButton = new ElevatorCarHelpCallButton(this);
        this.microphone = new ElevatorCarMicrophone();
        this.speaker = new ElevatorCarSpeaker();
        this.carPanel = carPanel;
    }

    public void pressHelpButton() {
        this.callButton.press();
    }

    public void turnHelpOn() {
        this.speaker.on();
        this.microphone.on();
    }

    public void turnHelpOff() {
        this.speaker.off();
        this.microphone.off();
    }


    public ElevatorCarPanel getCarPanel() {
        return carPanel;
    }

}
