package elevator.exceptions;

import elevator.enums.Direction;

public class ButtonNotActiveForDirection extends RuntimeException {
    public ButtonNotActiveForDirection(final Direction direction) {
        super(String.format("NoActiveButtonForDirection %s", direction.name()));
    }
}
