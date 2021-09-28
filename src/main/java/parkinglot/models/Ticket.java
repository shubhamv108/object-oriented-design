package parkinglot.models;

public class Ticket {

    private final int spotFloorNUmber;
    private final int spotNUmber;
    private final String vehicleRegNo;

    public Ticket(int spotFloorNUmber, int spotNUmber, String vehicleRegNo) {
        this.spotFloorNUmber = spotFloorNUmber;
        this.spotNUmber = spotNUmber;
        this.vehicleRegNo = vehicleRegNo;
    }

    public int getSpotFloorNUmber() {
        return spotFloorNUmber;
    }

    public int getSpotNUmber() {
        return spotNUmber;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "spotFloorNUmber=" + spotFloorNUmber +
                ", spotNUmber=" + spotNUmber +
                ", vehicleRegNo='" + vehicleRegNo + '\'' +
                '}';
    }
}
