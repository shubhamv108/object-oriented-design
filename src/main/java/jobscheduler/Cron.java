package jobscheduler;

import java.time.DayOfWeek;
import java.util.Optional;

public class Cron {
    private final Integer minute;
    private final Integer hour;
    private final DayOfWeek day;
    private final Integer month;
    private final Integer week;
    private final Integer year;


    public Cron(
            final Integer minute,
            final Integer hour,
            final DayOfWeek day,
            final Integer month,
            final Integer week,
            final Integer year) {
        this.validate(minute, hour, day, month, week, year);
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.week = week;
        this.year = year;
    }

    private void validate(
            final Integer minute,
            final Integer hour,
            final DayOfWeek day,
            final Integer month,
            final Integer week,
            final Integer year) {
        if (
                (minute != null && (minute < 0 || minute > 60)) ||
                (hour != null && (hour < 0 || hour > 24)) ||
                (month != null && (month < 1 || month > 12)) ||
                (week != null && (week < 1 || week > 52)) ||
                (year != null && (year < 1))
        )
            throw new IllegalArgumentException("invalid cron");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(Optional.ofNullable(this.minute).map(String::valueOf).orElse(("*"))).append(" ");
        builder.append(Optional.ofNullable(this.hour).map(String::valueOf).orElse(("*"))).append(" ");
        builder.append(Optional.ofNullable(this.day).map(DayOfWeek::getValue).map(String::valueOf).orElse(("*"))).append(" ");
        builder.append(Optional.ofNullable(this.month).map(String::valueOf).orElse(("*"))).append(" ");
        builder.append(Optional.ofNullable(this.week).map(String::valueOf).orElse(("*"))).append(" ");
        builder.append(Optional.ofNullable(this.year).map(String::valueOf).orElse(("*")));
        return builder.toString();
    }
}
