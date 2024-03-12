package usernamealreadytaken;

public interface ICacheService<K, V> {
    void put(K key, V value, long timeToLive);
    void put(K key, V value);
    V get(K key);
}