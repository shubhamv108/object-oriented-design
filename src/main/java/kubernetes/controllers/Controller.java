package kubernetes.controllers;

public interface Controller extends Runnable {
    void shutdown();
}
