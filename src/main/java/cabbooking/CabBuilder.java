package cabbooking;

public interface CabBuilder {
    CabBuilder withSource(String source);
    CabBuilder withDestination(String destination);
    CabBuilder withCabType(CabType cabType);
    CabBuilder withDriverRating(DriverRating rating);
    Cab build();
}
