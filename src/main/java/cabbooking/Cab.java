package cabbooking;

public final class Cab implements IRatable {
    private String source;
    private String destination;
    private CabType cabType;
    private DriverRating rating;

    public Cab(
            final String source,
            final String destination,
            final CabType cabType,
            final DriverRating rating) {
        this.source = source;
        this.destination = destination;
        this.cabType = cabType;
        this.rating = rating;
    }

    @Override
    public void addRating(final int rating) {
        this.rating.addRating(rating);
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public CabType getCabType() {
        return cabType;
    }

    public float getDriverRating() {
        return rating.getRating();
    }

    @Override
    public Cab clone() {
        return new CabConcreteBuilder()
                .withSource(source)
                .withDestination(destination)
                .withCabType(cabType)
                .withDriverRating(rating)
                .build();
    }

    @Override
    public String toString() {
        return "Cab{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", cabType=" + cabType +
                ", rating=" + rating +
                '}';
    }
}
