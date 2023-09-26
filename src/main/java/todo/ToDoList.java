package todo;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ToDoList {

    private final ArrayList<Task> tasks = new ArrayList<>();
    private final TreeMap<Long, ArrayList<String>> activityLog = new TreeMap();

    public void add(Task task) {
        this.tasks.add(task);
        task.setId(tasks.size() - 1);
        this.activityLog.computeIfAbsent(System.currentTimeMillis(), e -> new ArrayList<>()).add("Added task: " + task);
    }
    public void complete(int taskId) {
        this.activityLog.computeIfAbsent(System.currentTimeMillis(), e -> new ArrayList<>()).add("Completed task: " + taskId);
    }
    void remove(Task task) {}

    public void add(final List<Task> tasks) {
        tasks.forEach(task -> this.add(task));
    }
    public void modify(final List<Task> tasks) {
//        tasks.forEach(task -> this.modify(task));
    }

    public void remove(final List<Task> tasks) {
        tasks.forEach(task -> this.remove(task));
    }

}
