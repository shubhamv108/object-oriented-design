package airlinereservationsystem.actors;

import airlinereservationsystem.aircraft.Aircraft;
import airlinereservationsystem.flight.Flight;

public class Admin extends Person {

    public boolean addAircraft(Aircraft aircraft) {
        return false;
    }

    public boolean addFlight(Flight flight) {
        return false;
    }

    public boolean blockUser(Person person) {
        return false;
    }

}
