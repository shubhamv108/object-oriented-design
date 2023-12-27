package aws;

import aws.vpc.AvailabilityZone;

import java.util.Collection;
import java.util.LinkedHashSet;

public class Region {
    private final Collection<AvailabilityZone> availabilityZones = new LinkedHashSet<>();
}
