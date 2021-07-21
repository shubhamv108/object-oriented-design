package multilevelcache.evictionpolicies;

public interface IEvictionPolicy<Key , Value> {

    void access(Key key);

    Key remove(Key key);

    Key evict();

}
