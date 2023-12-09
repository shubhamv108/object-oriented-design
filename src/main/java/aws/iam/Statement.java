package aws.iam;

import aws.ARN;

import java.util.*;

public class Statement {
    private Optional<String> sid;
    private final Collection<Effect> effects = new ArrayList<>();

    /**
     * Account/User/Role to which the policy apply to
     */
    private final Map<String, Collection<String>> principals = new LinkedHashMap<>();
    private final Collection<Action> actions = new ArrayList<>();
    private final Collection<ARN> resources = new ArrayList<>();

    private Optional<Condition> condition;
}
