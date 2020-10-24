package vendingmachine.exceptions;

import vendingmachine.admin.observers.INotification;

public class MachineWarning extends RuntimeException implements INotification {
    private String errorMsg;

    public MachineWarning(String errMsg) {
        this.errorMsg = errMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String get() {
        return errorMsg;
    }
}
