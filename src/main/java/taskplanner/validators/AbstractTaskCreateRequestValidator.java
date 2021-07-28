package taskplanner.validators;

import commons.validators.AbstractRequestValidator;
import commons.validators.IValidator;
import taskplanner.entities.User;

import java.util.Date;

public abstract class AbstractTaskCreateRequestValidator<Task extends taskplanner.entities.Task> extends AbstractRequestValidator<Task> {

    @Override
    public IValidator<Task> validate(final Task task) {
        super.validate(task);
        String title = task.getTitle();
        User creator = task.getCreator();
        Date dueDate = task.getDueDate();
        if (title == null || title.length() == 0) {
            this.putMessage("title", MUST_NOT_BE_EMPTY, "title");
        }
        if (creator == null || creator.getUsername() == null || creator.getUsername().length() == 0) {
            this.putMessage("creator", MUST_NOT_BE_EMPTY, "creator");
        }
        if (dueDate == null) {
            this.putMessage("dueDate", MUST_NOT_BE_EMPTY, "dueDate");
        }
        return this;
    }
}
