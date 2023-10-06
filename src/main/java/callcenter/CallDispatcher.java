package callcenter;

public class CallDispatcher implements Runnable {

    private final CallCenter callCenter;

    public CallDispatcher(final CallCenter callCenter) {
        this.callCenter = callCenter;
    }

    @Override
    public void run() {
        this.callCenter.dispatchCall();
    }
}
