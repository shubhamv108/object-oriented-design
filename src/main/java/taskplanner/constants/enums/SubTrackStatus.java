package taskplanner.constants.enums;

public enum SubTrackStatus {

    OPEN(1), INPROGRESS(2), COMPLETED(3);

    private int sequenceNumber;

    SubTrackStatus(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

}
