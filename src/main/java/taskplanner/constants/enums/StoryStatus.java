package taskplanner.constants.enums;

public enum StoryStatus {

    OPEN(1), INPROGRESS(2), COMPLETED(3);

    private int sequenceNumber;

    StoryStatus(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
