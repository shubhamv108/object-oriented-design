package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.entities.Bug;

public class BugRepository extends AbstractHashKVStoreRepository<Integer, Bug> {

    public static final BugRepository INSTANCE = new BugRepository();

    private BugRepository() {}
}
