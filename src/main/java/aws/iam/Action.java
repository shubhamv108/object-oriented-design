package aws.iam;

import aws.Service;

public class Action {
    private final Service service;
    private final Operation operation;

    public Action(final Service service, final Operation operation) {
        this.service = service;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return service.toString() + ":" + operation.toString();
    }
}
