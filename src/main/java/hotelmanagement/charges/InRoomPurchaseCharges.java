package hotelmanagement.charges;

public class InRoomPurchaseCharges implements BaseRoomCharge {

    double cost;
    BaseRoomCharge baseRoomCharge;

    @Override
    public double getCost() {
        return this.baseRoomCharge.getCost() + cost;
    }
}
