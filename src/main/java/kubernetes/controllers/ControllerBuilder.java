package kubernetes.controllers;

import kubernetes.controllers.reconcilers.Reconciler;
import kubernetes.controllers.reconcilers.Request;
import kubernetes.informers.SharedInformerFactory;
import kubernetes.workqueues.WorkQueue;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ControllerBuilder {
    private int workerCount;
    private String controllerName;
    private WorkQueue<Request> workQueue;

    private SharedInformerFactory informerFactory;
    private List<Supplier<Boolean>> readyFuncs;
    private Reconciler reconciler;
//    public Controller build() throws IllegalStateException {
//        if (this.reconciler == null) {
//            throw new IllegalStateException("Missing reconciler when building controller.");
//        } else {
//            DefaultController controller = new DefaultController(this.reconciler, this.workQueue, (Supplier[])this.readyFuncs.stream().toArray((x$0) -> {
//                return new Supplier[x$0];
//            }));
//            controller.setName(this.controllerName);
//            controller.setWorkerCount(this.workerCount);
//            controller.setWorkerThreadPool(Executors.newScheduledThreadPool(this.workerCount, Controllers.namedControllerThreadFactory(this.controllerName)));
//            controller.setReconciler(this.reconciler);
//            return controller;
//        }
//    }
}
