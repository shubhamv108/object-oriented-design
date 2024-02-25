package jobscheduler;

public class Job {

    private String jarPath;
    private Long timeoutInMilliseconds;

    public Job(
            final String jarPath,
            final Long timeoutInMilliseconds) {
        this.jarPath = jarPath;
        this.timeoutInMilliseconds = timeoutInMilliseconds;
    }

    public String getJarPath() {
        return jarPath;
    }

    public Long getTimeoutInMilliseconds() {
        return timeoutInMilliseconds;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public void setTimeoutInMilliseconds(Long timeoutInMilliseconds) {
        this.timeoutInMilliseconds = timeoutInMilliseconds;
    }
}
