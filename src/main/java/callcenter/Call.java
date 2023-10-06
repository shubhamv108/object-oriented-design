package callcenter;

public class Call {

    private CallStatus status = CallStatus.READY;
    private Rank rank;
    private Employee assignedTo;

    public Call(final Rank rank) {
        this.rank = rank;
    }

    public void setAssignedTo(final Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void updateStatus(final CallStatus status) {
        this.status = status;
    }

    public Rank getRank() {
        return rank;
    }
}
