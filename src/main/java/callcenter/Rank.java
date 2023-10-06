package callcenter;

public enum Rank {

    OPERATOR(0),
    SUPERVISOR(1),
    DIRECTOR(2);

    private double rank;

    Rank(final double rank) {
        this.rank = rank;
    }

    public double getRank() {
        return rank;
    }
}
