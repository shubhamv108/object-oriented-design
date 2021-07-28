package taskplanner.services;

import taskplanner.entities.Sprint;
import taskplanner.entities.Task;

public interface ISprintService {

    Sprint create(Sprint sprint);
    Sprint delete(Sprint sprint);
    Sprint addTask(Sprint sprint, Task task);
    Sprint removeTask(Sprint sprint, Task task);
    Sprint get(String name);

    String display(String name);
}
