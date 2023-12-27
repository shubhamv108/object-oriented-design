package kubernetes.controllers.reconcilers;

import java.time.Duration;

public class Result {
    private boolean requeue;
    private Duration requeueAfter;
}