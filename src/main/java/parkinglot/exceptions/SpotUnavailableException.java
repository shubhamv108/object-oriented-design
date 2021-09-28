package parkinglot.exceptions;

import parkinglot.models.SpotType;

public class SpotUnavailableException extends RuntimeException {
    public SpotUnavailableException(SpotType spotType) {
        super(String.format("No spot available for spot type: %s", spotType));
    }
}
