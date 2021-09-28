package parkinglot2.gates;

import parkinglot2.accounts.Attendent;

public abstract class Gate {

    int gateId;
    Attendent attendent;

    public void setAttendent(Attendent attendent) {
        this.attendent = attendent;
    }
}
