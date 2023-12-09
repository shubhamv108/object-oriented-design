package aws.iam;

import aws.ITaggable;
import aws.Tag;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Stream;

public class User implements ITaggable {

    private final Password password = new Password();
    private final Collection<Group> groups = new LinkedHashSet<>();
    private final Collection<Policy> policies = new ArrayList<>();

    private final SecurityCredentials securityCredentials = new SecurityCredentials();

    private final AccessAdvisor accessAdvisor = new AccessAdvisor();

    public Stream<Policy> getPolicies() {
        return Stream.concat(
                policies.stream(),
                groups.stream().flatMap(Group::getPolicies));
    }

    @Override
    public void addTag(final Tag tag) {

    }

    @Override
    public Collection<Tag> getTags() {
        return null;
    }

    public void removeGroup(final Group group) {
        if (this.groups.contains(group))
            this.groups.remove(group);
    }
}
