package aws.iam;

import org.checkerframework.checker.units.qual.A;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

public class Group {
    private final Collection<User> users = new LinkedHashSet<>();

    private final Collection<Policy> policies = new ArrayList<>();

    public Stream<Policy> getPolicies() {
        return policies.stream();
    }

    public void removeUser(final User user) {
        this.users.remove(user);
    }
}
