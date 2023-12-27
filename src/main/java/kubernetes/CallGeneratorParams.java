package kubernetes;

public class CallGeneratorParams {

    public Boolean watch;
    public String resourceVersion;
    public Integer timeoutSeconds;

    public CallGeneratorParams(
            final Boolean watch,
            final String resourceVersion,
            final Integer timeoutSeconds) {
        this.watch = watch;
        this.resourceVersion = resourceVersion;
        this.timeoutSeconds = timeoutSeconds;
    }
}
