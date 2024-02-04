package cabbooking;

public class CabConcreteBuilder implements CabBuilder {

    private String source;
    private String destination;
    private CabType cabType;
    private DriverRating rating;

    @Override
    public CabBuilder withSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public CabBuilder withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public CabBuilder withCabType(CabType cabType) {
        this.cabType = cabType;
        return this;
    }

    @Override
    public CabBuilder withDriverRating(DriverRating rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public Cab build() {
        return new Cab(source, destination, cabType, rating);
    }
}
