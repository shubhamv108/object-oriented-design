package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.entities.Story;

public class StoryRepository extends AbstractHashKVStoreRepository<Integer, Story> {

    public static final StoryRepository INSTANCE = new StoryRepository();

    private StoryRepository() {}
}
