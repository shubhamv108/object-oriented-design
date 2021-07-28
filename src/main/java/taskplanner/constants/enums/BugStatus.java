package taskplanner.constants.enums;

public enum BugStatus {

    OPEN(1), INPROGREES(1), FIXED(2);

    private int sequenceNumber;

    BugStatus(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

}
