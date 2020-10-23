package vendingmachine.exceptions;

public class MachineWarning extends RuntimeException {
    private String errorMsg;

    public MachineWarning(String errMsg) {
        this.errorMsg = errMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
