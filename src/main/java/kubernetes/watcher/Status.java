package kubernetes.watcher;

import kubernetes.objects.ObjectListMeta;

public class Status {
    private String kind;
    private String message;
    private ObjectListMeta metadata;
    private String reason;
    private String status;
}
