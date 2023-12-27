package aws.billingandcostmngmnt;

import aws.Service;

public class FreeTier {
    Service service;
    long limitInKB;
    long currentLimit;
    long currentUsage;
    long forecastedUsage;
}
