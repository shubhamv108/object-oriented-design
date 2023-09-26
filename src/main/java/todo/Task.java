package todo;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Task {

    private Integer id;

    private Date deadline;
    private final Collection<String> tags = new HashSet<>();

    private final ToDoList toDoList;

    private final User user;

    private TaskStatus status = TaskStatus.TODO;

    public Task(final User user, final ToDoList toDoList) {
        this(user, null, toDoList);
    }

    public Task(final User user, final Date deadline, ToDoList toDoList) {
        this.user = user;
        this.deadline = deadline;
        this.toDoList = toDoList;
    }

    public void complete() {
        this.status = TaskStatus.COMPLETED;
        this.toDoList.remove(this);
    }

    protected void setId(final Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
}
