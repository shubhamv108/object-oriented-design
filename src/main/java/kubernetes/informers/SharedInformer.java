package kubernetes.informers;

import kubernetes.objects.KubernetesObject;

interface SharedInformer<ApiType extends KubernetesObject> {

    void addEventHandler(ResourceEventHandler<ApiType> handler);

    void addEventHandlerWithResyncPeriod(ResourceEventHandler<ApiType> handler, long resyncPeriod);

    void run();

    void stop();

    boolean hasSynced();

    String lastSyncResourceVersion();

    void setTransform(TransformFunc transformFunc);
}
