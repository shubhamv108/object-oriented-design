package aws.recyclebin;

import java.time.Period;

public class RetentionRule {
    String name;
    String ruleID;
    String description;
    Period retention;
    String lockState;
}
