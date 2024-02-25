package jobscheduler;

import order.OrderStateFactory.SingletonHolder;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScheduleManager {
    private final Map<String, Schedule> schedules = new HashMap<>();
    private final Map<String, Map<String, Schedule>> jobSchedules = new HashMap<>();

    public static ScheduleManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final ScheduleManager INSTANCE = new ScheduleManager();
    }

    public void create(final Schedule schedule) {
        final String scheduleId = UUID.randomUUID().toString();
        this.schedules.put(scheduleId, schedule);
        this.jobSchedules.computeIfAbsent(schedule.getJobId(), k -> new HashMap<>())
                .put(scheduleId, schedule);
    }

    public List<Schedule> getAllBetweenStartDateAndEndDate(final Instant instant) {
        return this.schedules
                .values()
                .stream()
                .filter(schedule -> schedule.getStartDate().isBefore(instant) || schedule.getStartDate().equals(instant))
                .filter(schedule -> schedule.getEndDate().isAfter(instant) || schedule.getEndDate().equals(instant))
                .sorted((x, y) -> x.getStartDate().compareTo(y.getEndDate()))
                .toList();
    }

    public Schedule getById(final String id) {
        return Optional.ofNullable(this.schedules.get(id))
                .orElseThrow(IllegalArgumentException::new);
    }

    public Collection<Schedule> getAllByJobId(final String jobId) {
        return Optional.ofNullable(this.jobSchedules.get(jobId))
                .map(Map::values)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Schedule delete(final String id) {
        return this.schedules.remove(id);
    }
}
