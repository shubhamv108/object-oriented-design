package loadbalancer.exceptions;

public class NoSuchBackendExists extends RuntimeException {
    public NoSuchBackendExists(String name) {
        super(String.format("No backend exists with name: %s", name));
    }
}
