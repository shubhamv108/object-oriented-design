package airlinemanagement.controllers;

import airlinemanagement.apis.SeatView;
import airlinemanagement.services.FlightInstanceService;

import java.time.Instant;

public class FlightInstantController {

    public SeatView getSeatView(final String flightNumber, final Instant departureInstant) {
        return FlightInstanceService.getInstant().getSeatView(flightNumber, departureInstant);
    }

}
