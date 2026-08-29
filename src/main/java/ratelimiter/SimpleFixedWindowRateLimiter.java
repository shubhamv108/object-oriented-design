package ratelimiter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class CacheEntry<V> {
    protected V v;
    private Long expiryAtInNanos;

    public void set(V v, Long time, TimeUnit timeUnit) {
        this.v = v;
        this.expiryAtInNanos = System.nanoTime() + timeUnit.toNanos(time);
    }

    public V get() {
        if (isExpired())
            return null;
        return v;
    }

    public boolean isExpired() {
        return System.nanoTime() >= expiryAtInNanos;
    }
}

class IntegerCacheEntry extends CacheEntry<Integer> {
    public void increment() {
        ++v;
    }
}

class Cache<V> {
    protected final Map<String, CacheEntry<V>> cache = new ConcurrentHashMap<>();

    public void set(String key, V v, Long duration, TimeUnit timeUnit) {
        cache.computeIfAbsent(key, e -> new CacheEntry<>()).set(v, duration, timeUnit);
    }

    public V get(String key) {
        return Optional.ofNullable(cache.get(key)).map(CacheEntry::get).orElse(null);
    }
}

class IntegerCache extends Cache<Integer> {
    @Override
    public void set(String key, Integer v, Long duration, TimeUnit timeUnit) {
        cache.computeIfAbsent(key, e -> new IntegerCacheEntry()).set(v, duration, timeUnit);
    }
    public void increment(String key) {
        Optional.ofNullable(cache.get(key))
                .filter(IntegerCacheEntry.class::isInstance)
                .map(IntegerCacheEntry.class::cast)
                .ifPresent(IntegerCacheEntry::increment);
    }
}

class User {
    private final String userId;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public User(String userId) {
        this.userId = userId;
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}

public class SimpleFixedWindowRateLimiter {
    public class RateLimiter {
        private final IntegerCache cache = new IntegerCache();
        private final Map<String, User> users = new ConcurrentHashMap<>();

        public boolean isAllowed(String userId) {
            User user = users.computeIfAbsent(userId, User::new);
            String key = "rate:" + userId;

            user.getLock().readLock().lock();
            try {
                Integer count = cache.get(key);
                if (count != null && count >= 100)
                    return false;
            } finally {
                user.getLock().readLock().unlock();
            }

            user.getLock().writeLock().lock();
            try {
                Integer count = cache.get(key);
                if (count == null) {
                    cache.set(key, 1, 60L, TimeUnit.SECONDS);
                    return true;
                }

                if (count >= 100)
                    return false;

                cache.increment(key);
                return true;
            } finally {
                user.getLock().writeLock().unlock();
            }
        }
    }
}
