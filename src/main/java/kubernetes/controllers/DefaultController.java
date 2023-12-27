package kubernetes.controllers;

import kubernetes.controllers.reconcilers.Reconciler;
import kubernetes.controllers.reconcilers.Request;
import kubernetes.workqueues.WorkQueue;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class DefaultController {
    private Reconciler reconciler;
    private String name;
    private int workerCount;
    private ScheduledExecutorService workerThreadPool;
    private WorkQueue<Request> workQueue;
    private Supplier<Boolean>[] readyFuncs;
    private Duration readyTimeout;
    private Duration readyCheckInternal;
}
