package jobscheduler;

import java.time.Duration;
import java.time.Instant;

public class Executable {
    private Instant instant;
    private Status status;
    private String message;
    private Instant pickTime;
    private Duration executionTime;
    private Instant finishTime;
}
