package elevator.car.panels;

import elevator.car.ElevatorCar;
import elevator.car.buttons.ElevatorCarAlarmButton;
import elevator.car.buttons.ElevatorCarDoorCloseButton;
import elevator.car.buttons.ElevatorCarDoorOpenButton;
import elevator.car.buttons.ElevatorCarFanButton;
import elevator.car.buttons.ElevatorCarFloorButton;
import elevator.car.buttons.ElevatorCarLightButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class ElevatorCarPanel {

    private final Map<Integer, ElevatorCarFloorButton> floorButtons;
    private final ElevatorCarDoorOpenButton doorOpenButton;
    private final ElevatorCarDoorCloseButton doorCloseButton;
    private final ElevatorCarAlarmButton alarmButton;
    private final ElevatorCarFanButton fanButton;
    private final ElevatorCarLightButton lightButton;
    private final ElevatorCarHelpPanel helpPanel;
    private final ElevatorCarDisplayPanel displayPanel;
    private final ElevatorCar elevatorCar;

    public ElevatorCarPanel(final int lowestFloorNumber,
                            final int highestFloorNumber,
                            final Set<Integer> activeFloors,
                            final ElevatorCar elevatorCar) {
        this.floorButtons = new HashMap<>();
        IntStream.rangeClosed(lowestFloorNumber, highestFloorNumber)
                .forEach(floorNumber -> {
                        ElevatorCarFloorButton button = new ElevatorCarFloorButton(this, floorNumber);
                        this.floorButtons.put(floorNumber, button);
                        if (activeFloors.contains(floorNumber)) button.deactivate();
                });
        this.doorOpenButton = new ElevatorCarDoorOpenButton(this);
        this.doorCloseButton = new ElevatorCarDoorCloseButton(this);
        this.alarmButton = new ElevatorCarAlarmButton(this);
        this.fanButton = new ElevatorCarFanButton(this);
        this.lightButton = new ElevatorCarLightButton(this);
        this.helpPanel = new ElevatorCarHelpPanel(this);
        this.displayPanel = new ElevatorCarDisplayPanel();
        this.elevatorCar = elevatorCar;
    }

    public boolean pressFloorButton(final int floorNumber) {
        ElevatorCarFloorButton floorButton = this.floorButtons.get(floorNumber);
        if (floorButton != null)
            return floorButton.press();
        return false;
    }

    public void pressAlarm(final Long milliSeconds) throws InterruptedException {
        this.alarmButton.press(milliSeconds);
    }

    public void pressFanButton() {
        this.fanButton.press();
    }

    public void pressLightButton() {
        this.lightButton.press();
    }

    public void presHelpButton() {
        this.helpPanel.pressHelpButton();
    }

    public void openDoor() {
        this.doorOpenButton.press();
    }

    public void closeDoor() {
        this.doorOpenButton.press();
    }

    public ElevatorCar getElevatorCar() {
        return elevatorCar;
    }
}
