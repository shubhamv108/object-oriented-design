package kubernetes.watcher;

import kubernetes.CallGeneratorParams;
import kubernetes.objects.KubernetesListObject;
import kubernetes.objects.KubernetesObject;
import kubernetes.watcher.Watchable;

public interface ListerWatcher<ApiType extends KubernetesObject, ApiListType extends KubernetesListObject> {

    ApiListType list(CallGeneratorParams params) throws RuntimeException;

    Watchable<ApiType> watch(CallGeneratorParams params) throws RuntimeException;
}
