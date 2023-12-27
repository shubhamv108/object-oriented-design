package kubernetes.objects;

import java.time.OffsetDateTime;
import java.util.Map;

public class ObjectMeta {
    private Map<String, String> annotations = null;

    private OffsetDateTime creationTimestamp;
}
