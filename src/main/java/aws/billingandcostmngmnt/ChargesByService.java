package aws.billingandcostmngmnt;

import aws.Regions;
import aws.Service;

import java.math.BigDecimal;
import java.util.Map;

public class ChargesByService {
    private Service service;
    private Map<Regions, BigDecimal> charges;
}
