package multilevelcache.caches;

public interface ICacheProvider<Key, Value> {

   Value set(Key key, Value value);
   Value get(Key key);
   Value remove(Key key);
   Key evict();
   int getFreeCapacity();
   void setMetricEmitter(MetricEmitter metricEmitter);

}
