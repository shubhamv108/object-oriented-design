package aws.vpc;

import aws.ITaggable;
import aws.Tag;

import java.util.Collection;

public class Subnet implements ITaggable {

    private String name;

    private AvailabilityZone availabilityZone;

    private String IPv4CIDR;

    private VPC vpc;

    @Override
    public void addTag(Tag tag) {

    }

    @Override
    public Collection<Tag> getTags() {
        return null;
    }
}
