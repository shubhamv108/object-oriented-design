package aws.iam;

import aws.Service;

import java.util.Collection;
import java.util.Date;

public class AccessAdvisor {

    private Collection<AccessAdvisorRecord> accessAdvisorRecords;

    private class AccessAdvisorRecord {
        private Service service;
        private Policy policyGrantingPermisison;
        private Date lasstAccessed;
    }
}
