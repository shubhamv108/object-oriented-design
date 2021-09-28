package elevator.components.buttons;

public class ToggleButton extends Button {
    public boolean press() {
        if (this.isActive) {
            this.isPressed = !this.isPressed;
            this.onPress();
            return true;
        }
        return false;
    }

    @Override
    public void onPress() {

    }
}
