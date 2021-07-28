package taskplanner.repositories;

import commons.repositories.AbstractKVStoreRepository;
import taskplanner.entities.Feature;

public class FeatureRepository extends AbstractKVStoreRepository<Integer, Feature> {

    public static final FeatureRepository INSTANCE = new FeatureRepository();

    private FeatureRepository() {}
}
