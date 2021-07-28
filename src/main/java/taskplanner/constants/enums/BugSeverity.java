package taskplanner.constants.enums;

public enum BugSeverity {

    P0(1), P1(2), P2(3);

    private int severity;

    BugSeverity(int severity) {
        this.severity = severity;
    }

    public int getSeverity() {
        return severity;
    }
}
