package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.exceptions.SprintAlreadyExistException;
import taskplanner.entities.Sprint;

public class SprintRepository extends AbstractHashKVStoreRepository<String, Sprint> {

    public static final SprintRepository INSTANCE = new SprintRepository();

    private SprintRepository() {}

    @Override
    public synchronized Sprint save(final Sprint sprint) {
        Sprint s = this.getStore().get(sprint.getName());
        if (s == null) {
            return this.put(sprint.getName(), sprint);
        } else {
            throw new SprintAlreadyExistException(sprint);
        }
    }

}
