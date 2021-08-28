package elevator.components.buttons;

import elevator.enums.Direction;

public class DirectionButton extends Button {
    private final Direction direction;

    public DirectionButton(final Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void onPress() {

    }
}
