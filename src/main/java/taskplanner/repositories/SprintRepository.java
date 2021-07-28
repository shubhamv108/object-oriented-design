package taskplanner.repositories;

import commons.repositories.AbstractKVStoreRepository;
import taskplanner.exceptions.SprintAlreadyExistException;
import taskplanner.entities.Sprint;

public class SprintRepository extends AbstractKVStoreRepository<String, Sprint> {

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
