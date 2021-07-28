package taskplanner.repositories;

import commons.repositories.AbstractKVStoreRepository;
import taskplanner.entities.SubTrack;

public class SubTrackRepository extends AbstractKVStoreRepository<Integer, SubTrack> {

    public static final SubTrackRepository INSTANCE = new SubTrackRepository();

    private SubTrackRepository() {}
}
