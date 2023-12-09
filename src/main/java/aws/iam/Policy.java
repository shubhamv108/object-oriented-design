package aws.iam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Allows defining permission of a user.
 * apply The Least privilege Principle
 *
 * AdministratorAccess
 */
public class Policy {
    private String version;
    private Optional<String> id;
    private final Collection<Statement> statements = new ArrayList<>();
}
