package kubernetes.objects;

public interface KubernetesObject extends KubernetesType {
    ObjectMeta getMetadata();
}

