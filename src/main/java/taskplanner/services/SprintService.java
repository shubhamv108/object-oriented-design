package taskplanner.services;

import taskplanner.exceptions.TaskPlannerException;
import taskplanner.locks.TaskPlannerLockManager;
import taskplanner.entities.Sprint;
import taskplanner.entities.Task;
import taskplanner.repositories.SprintRepository;

import java.util.Optional;

public enum SprintService implements ISprintService {
    INSTANCE;

    private final SprintRepository sprintRepository;
    private final TaskService taskService;

    SprintService() {
        this(SprintRepository.INSTANCE, TaskService.INSTANCE);
    }

    SprintService(final SprintRepository sprintRepository, final TaskService taskService) {
        this.sprintRepository = sprintRepository;
        this.taskService = taskService;
    }

    @Override
    public Sprint create(final Sprint sprint) {
        if (sprint.getName() == null || sprint.getName().length() == 0) throw new TaskPlannerException("sprint name is empty");
        Sprint existing = this.sprintRepository.getByKey(sprint.getName());
        if (existing != null && !existing.isDeleted()) {
            throw new TaskPlannerException(String.format("Sprint with %s already exists", existing.getName()));
        }
        return this.sprintRepository.save(sprint);
    }

    @Override
    public Sprint delete(final Sprint sprint) {
        if (sprint.getName() == null || sprint.getName().length() == 0) throw new TaskPlannerException("sprint name is empty");
        Sprint existing;
        try {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().lock();
            existing = this.sprintRepository.getByKey(sprint.getName());
            if (existing == null || existing.isDeleted())
                throw new TaskPlannerException(String.format("Sprint with name %s does not exists", sprint.getName()));
            existing.delete();
            existing = this.sprintRepository.save(existing);
        } finally {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Sprint addTask(Sprint sprint, Task task) {
        if (sprint.getName() == null || sprint.getName().length() == 0) throw new TaskPlannerException("sprint name is empty");
        if (task == null || task.getId() == null) throw new TaskPlannerException("task is empty");
        Sprint existing = null;
        try {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().lock();
            existing = this.sprintRepository.getByKey(sprint.getName());
            if (existing == null || existing.isDeleted())
                throw new TaskPlannerException(String.format("Sprint with name %s does not exists", sprint.getName()));
            try {
                TaskPlannerLockManager.get(task).writeLock().lock();
                Task existingTask = this.taskService.get(task);
                if (existingTask == null) throw new TaskPlannerException("No such task present");
                if (!existing.hasTask(existingTask)) {
                    existing.addTask(existingTask);
                    existingTask.setSprint(existing);
                }
            } finally {
                TaskPlannerLockManager.get(task).writeLock().unlock();
            }
        } finally {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Sprint removeTask(final Sprint sprint, final Task task) {
        if (sprint.getName() == null || sprint.getName().length() == 0) throw new TaskPlannerException("sprint name is empty");
        if (task == null || task.getId() == null) throw new TaskPlannerException("task is empty");
        Sprint existing;
        try {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().lock();
            existing = this.sprintRepository.getByKey(sprint.getName());
            if (existing == null || existing.isDeleted())
                throw new TaskPlannerException(String.format("Sprint with name %s does not exists", sprint.getName()));
            try {
                TaskPlannerLockManager.get(task).writeLock().lock();
                Task existingTask = this.taskService.get(task);
                if (existingTask == null) throw new TaskPlannerException("No such task present");
                existing.removeTask(existingTask);
                existingTask.setSprint(null);
            } finally {
                TaskPlannerLockManager.get(task).writeLock().unlock();
            }
        } finally {
            TaskPlannerLockManager.get("SPRINT-" + sprint.getName()).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Sprint get(final String name) {
        return this.sprintRepository.getByKey(name);
    }

    @Override
    public String display(final String name) {
        return Optional.ofNullable(this.get(name)).map(e -> e.toString())
                .orElseThrow(() -> new TaskPlannerException("No such task"));
    }
}
