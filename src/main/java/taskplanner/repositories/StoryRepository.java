package taskplanner.repositories;

import commons.repositories.AbstractKVStoreRepository;
import taskplanner.entities.Story;

public class StoryRepository extends AbstractKVStoreRepository<Integer, Story> {

    public static final StoryRepository INSTANCE = new StoryRepository();

    private StoryRepository() {}
}
