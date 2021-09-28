package hotelmanagement.charges;

public class RoomServiceCharge implements BaseRoomCharge {

    double cost;
    BaseRoomCharge baseRoomCharge;

    @Override
    public double getCost() {
        return this.baseRoomCharge.getCost() + cost;
    }
}
