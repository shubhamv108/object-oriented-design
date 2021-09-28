package parkinglot2;

import java.util.Date;

public class Ticket {

    String licenseNumber;
    int lotId;
    int floorNumber;
    int spaceNumber;
    Date entryTime;
    Date exitTime;
    SpaceType spaceType;
    double totalCost;
    TicketStatus ticketStatus;

    public void totalCost() {}
    public void updateVehicleExitTime(Date exitTime) {}

}
