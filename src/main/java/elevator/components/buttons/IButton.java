package elevator.components.buttons;

public interface IButton {
    boolean press();

    void deactivate();

    boolean isActive();

    public void onPress();
}
