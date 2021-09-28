package elevator.components.buttons;

public class FloorButton extends Button {
    protected int floorNumber;
    public FloorButton(final int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    @Override
    public void onPress() {

    }
}
