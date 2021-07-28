package taskplanner.repositories;

import commons.repositories.AbstractKVStoreRepository;
import taskplanner.entities.Bug;

public class BugRepository extends AbstractKVStoreRepository<Integer, Bug> {

    public static final BugRepository INSTANCE = new BugRepository();

    private BugRepository() {}
}
