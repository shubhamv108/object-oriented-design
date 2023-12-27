package kubernetes.objects;

public interface KubernetesType {
    String getApiVersion();

    String getKind();
}
