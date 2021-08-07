package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.entities.SubTrack;

public class SubTrackRepository extends AbstractHashKVStoreRepository<Integer, SubTrack> {

    public static final SubTrackRepository INSTANCE = new SubTrackRepository();

    private SubTrackRepository() {}
}
