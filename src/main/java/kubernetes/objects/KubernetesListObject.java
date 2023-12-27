package kubernetes.objects;

import java.util.List;

public interface KubernetesListObject extends KubernetesType {
    ObjectListMeta getMetadata();

    List<? extends KubernetesObject> getItems();
}
