package aws.billingandcostmngmnt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Budget {
    private String name;
    private String email;

    private BigDecimal maxSpend;
    private Collection<Double> notificationPercentages = new ArrayList<>();
}
