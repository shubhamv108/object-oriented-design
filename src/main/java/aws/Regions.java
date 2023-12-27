package aws;

public enum Regions {
    AP_SOUTH_1("ap-south-1");

    private String region;
    Regions(final String region) {
        this.region = region;
    }
}
