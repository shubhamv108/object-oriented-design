package aws.iam;

import java.util.ArrayList;
import java.util.Collection;

public class Policies {

    private Collection<Policy> policies = new ArrayList<>();

    public void addPolicy(final Policy policy) {
        this.policies.add(policy);
    }

}
