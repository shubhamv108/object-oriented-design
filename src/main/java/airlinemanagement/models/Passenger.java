package airlinemanagement.models;

import java.util.Date;

public class Passenger {
    private String name;
    private String identityNumber;
    private String passportNumber;
    private Date dob;

    public Passenger(
            final String name,
            final String identityNumber,
            final String passportNumber,
            final Date dob) {
        this.name = name;
        this.identityNumber = identityNumber;
        this.passportNumber = passportNumber;
        this.dob = dob;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }
}
