package aws.ec2;

import aws.ITaggable;
import aws.Tag;

import java.util.Collection;

public class EC2Instance implements ITaggable {

    private EC2InstanceType instanceType;
    private EC2instanceConfiguration instanceConfiguration;

    @Override
    public void addTag(Tag tag) {

    }

    @Override
    public Collection<Tag> getTags() {
        return null;
    }
}
