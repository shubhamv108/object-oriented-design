package aws.billingandcostmngmnt;

import java.util.ArrayList;
import java.util.Collection;

public class Bills {
    private final Collection<ChargesByService> chargesByServices = new ArrayList<>();

    private final Collection<FreeTier> freeTiers = new ArrayList<>();
}
