package cabbooking;

public final class DriverRating {
    private int totalRides;
    private int totalRating;

    public void addRating(final int rating) {
        synchronized (this) {
            this.totalRides += 1;
            this.totalRating += rating;
        }
    }

    public float getRating() {
        if (totalRides == 0)
            return 5.0f;
        return ((float) this.totalRating / (float) totalRides);
    }

    @Override
    public String toString() {
        return String.valueOf(this.getRating());
    }
}
