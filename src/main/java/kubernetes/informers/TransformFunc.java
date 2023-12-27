package kubernetes.informers;

import kubernetes.objects.KubernetesObject;

public interface TransformFunc {

    KubernetesObject transform(KubernetesObject object) throws RuntimeException;
}
