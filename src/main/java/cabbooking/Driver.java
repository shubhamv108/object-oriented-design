package cabbooking;

public class Driver {

    private String source;
    private String destination;
    private CabType cabType;
    private float rating;

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public CabType getCabType() {
        return cabType;
    }

    public float getRating() {
        return rating;
    }

    public void updateRating(long updateBy) {
        this.rating += updateBy;
    }
}
