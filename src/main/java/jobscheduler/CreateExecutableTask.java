package jobscheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class CreateExecutableTask implements Runnable {
    @Override
    public void run() {
        ScheduleManager.getManager()
                .getAllBetweenStartDateAndEndDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                        .stream()
                        .map(schedule -> new Executable());

//        ExecutableManager.getManager().add();

    }
}
