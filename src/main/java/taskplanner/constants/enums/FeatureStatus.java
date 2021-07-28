package taskplanner.constants.enums;

public enum FeatureStatus {

    OPEN(1), INPROGREES(2), TESTING(3), DEPLOYED(4);

    private int sequenceNumber;

    FeatureStatus(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

}
