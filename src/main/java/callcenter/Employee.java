package callcenter;

public class Employee {

    private Call call;

    public boolean isFree() {
        return this.call == null;
    }

    public void takeCall(final Call call) {
        this.call = call;
        this.call.setAssignedTo(this);
        this.call.updateStatus(CallStatus.IN_PROGRESS);
    }
}
