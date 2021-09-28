package parkinglot.models;

import parkinglot.exceptions.InvalidTicketException;
import parkinglot.exceptions.SpotUnavailableException;
import parkinglot.exceptions.VehicleAlreadyParkedException;
import parkinglot.startegies.IParkingStrategy;

public class ParkingLot implements IParkingLot {

    private final IParkingStrategy parkingStrategy;
    private final Vehicles vehicles;

    public ParkingLot(IParkingStrategy parkingStrategy, Vehicles vehicles) {
        this.parkingStrategy = parkingStrategy;
        this.vehicles = vehicles;
    }

    @Override
    public Ticket park(Vehicle vehicle, SpotType spotType) {
        if (this.vehicles.isPresent(vehicle.getRegNo())) {
            throw new VehicleAlreadyParkedException(vehicle.getRegNo());
        }
        Spot spot = this.parkingStrategy.getNextAvailableSpotBySpotType(spotType);
        if (spot == null) {
            throw new SpotUnavailableException(spotType);
        }
        spot.setVehicle(vehicle);
        this.vehicles.add(vehicle);
        this.parkingStrategy.makeSpotOccupied(spot);
        return new Ticket(spot.getFloorNUmber(), spot.getSpotNumber(), vehicle.getRegNo());
    }

    @Override
    public Vehicle unPark(Ticket ticket) {
        Spot spot = this.parkingStrategy.getOccupiedSpot(ticket.getSpotFloorNUmber(), ticket.getSpotNUmber());
        if (spot == null) {
            throw new InvalidTicketException(ticket, "Spot unoccupied");
        }
        if (!spot.getVehicle().getRegNo().equals(ticket.getVehicleRegNo())) {
            throw new InvalidTicketException(ticket);
        }
        Vehicle vehicle = spot.getVehicle();
        spot.setVehicle(null);
        this.vehicles.remove(vehicle);
        this.parkingStrategy.makeSpotAvailable(spot);
        return vehicle;
    }

    @Override
    public void addSpot(int floorNumber, int spotNumber, SpotType type) {
        Spot spot = new Spot(floorNumber, spotNumber, type);
        this.parkingStrategy.addSpot(spot);
    }
}
