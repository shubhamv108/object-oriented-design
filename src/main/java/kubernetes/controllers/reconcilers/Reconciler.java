package kubernetes.controllers.reconcilers;

public interface Reconciler {

    /**
     * Reconcile result.
     *
     * @param request the reconcile request, triggered by watch events
     * @return the result
     */
    Result reconcile(Request request);
}
