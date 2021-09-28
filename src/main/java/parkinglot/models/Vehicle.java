package parkinglot.models;

public class Vehicle {
    private final String regNo;
    private String color;

    public Vehicle(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "regNo='" + regNo + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
