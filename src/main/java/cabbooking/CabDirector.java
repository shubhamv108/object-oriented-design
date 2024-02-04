package cabbooking;

public class CabDirector {

    private CabBuilder builder;

    public CabDirector(final CabBuilder builder) {
        this.builder = builder;
    }

    public Cab constructCab(String source, String destination, CabType cabType, DriverRating rating) {
        return builder
                .withSource(source)
                .withDestination(destination)
                .withCabType(cabType)
                .withDriverRating(rating)
                .build();
    }

}
