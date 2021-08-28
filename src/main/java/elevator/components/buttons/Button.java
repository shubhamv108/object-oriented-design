package elevator.components.buttons;

public abstract class Button implements IButton {

    protected boolean isPressed;
    protected boolean isActive = true;

    @Override
    public boolean press() {
        if (this.isActive && !this.isPressed) {
            this.isPressed = true;
            this.onPress();
            return true;
        }
        return false;
    }

    @Override
    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
