package cabbooking;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

public class CabBookingSystem {

    public static void main(String[] args) {
        final CabConcreteBuilder cabBuilder = new CabConcreteBuilder();
        final CabDirector cabDirector = new CabDirector(cabBuilder);
        final Cab cab = cabDirector.constructCab("A", "B", CabType.SUV, new DriverRating());

        CabManager.getInstance().add(cab);

        final User user = new User();
        Set<Entry<String, Cab>> cabs = user.findCabs("A", "B", Arrays.asList(CabType.SUV));
        System.out.println(cabs);

        final String selectedCabId = cabs.stream().findAny().get().getKey();

        user.confirm(selectedCabId);
        user.endTrip(selectedCabId, 4);

        user.confirm(selectedCabId);
        user.cancel(selectedCabId);
    }

}
