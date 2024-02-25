package jobscheduler;

import org.checkerframework.checker.units.qual.C;

import java.time.Instant;
import java.util.Date;

public class Schedule {

    private String jobId;
    private Cron cron;
    private Instant startDate;
    private Instant endDate;

    public Schedule(
            final String jobId,
            final Cron cron,
            final Instant startDate,
            final Instant endDate) {
        this.jobId = jobId;
        this.cron = cron;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getJobId() {
        return jobId;
    }

    public Cron getCron() {
        return cron;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
