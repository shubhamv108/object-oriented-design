package todo;

import java.util.List;

public class User {

    private final ToDoList toDos = new ToDoList();

    void add(Task task) {

    }
    void modify(Task task) {}
    void remove(Task task) {}

    public void add(final List<Task> tasks) {
        tasks.forEach(task -> this.add(task));
    }
    public void modify(final List<Task> tasks) {
        tasks.forEach(task -> this.modify(task));
    }

    public void remove(final List<Task> tasks) {
        tasks.forEach(task -> this.remove(task));
    }
}
