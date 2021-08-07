package taskplanner.repositories;

import commons.repositories.AbstractHashKVStoreRepository;
import taskplanner.entities.Feature;

public class FeatureRepository extends AbstractHashKVStoreRepository<Integer, Feature> {

    public static final FeatureRepository INSTANCE = new FeatureRepository();

    private FeatureRepository() {}
}
