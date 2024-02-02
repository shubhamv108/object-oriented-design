package cabbooking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverManager {


    private List<Driver> available = new ArrayList<>();
    private List<Driver> booked = new ArrayList<>();

    public Optional<Driver> filter(String source, String destination, List<CabType> cabTypes) {
        return available.stream()
                .filter(driver -> source.equals(driver.getSource()))
                .filter(driver -> destination.equals(driver.getDestination()))
                .filter(driver -> cabTypes.contains(driver.getCabType()))
                .findFirst();
    }

    public void setBooked(Driver driver) {

    }
}
