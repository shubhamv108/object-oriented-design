package kubernetes.informers;

import kubernetes.objects.KubernetesObject;

public interface ResourceEventHandler<ApiType extends KubernetesObject> {

    void onAdd(ApiType obj);

    void onUpdate(ApiType oldObj, ApiType newObj);

    void onDelete(ApiType obj, boolean deletedFinalStateUnknown);
}

