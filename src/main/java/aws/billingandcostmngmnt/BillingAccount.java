package aws.billingandcostmngmnt;

import aws.Regions;

import java.util.ArrayList;
import java.util.Collection;

public class BillingAccount {
    private final AccountSettings accountSettings = new AccountSettings();

    private final ContactInformation contactInformation = new ContactInformation();
    private String alternateContacts;

    private Collection<SecurityQuestion> securityQuestions = new ArrayList<>();

    private Collection<Regions> enabledRegions = new ArrayList<>();

    private boolean isIAMGivenAccessToBillingInfo;

    public void activateIAMAccess() {
        this.isIAMGivenAccessToBillingInfo = true;
    }
}
