package usernamealreadytaken.cache;

import lombok.Builder;
import lombok.Getter;
import usernamealreadytaken.ICacheService;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTTLCache<K, V extends Object> implements ICacheService<K, V> {

    @Builder
    @Getter
    private static class CachedEntry<V> {
        private V value;
        private Long expiryAt;

        public V value() {
            if (isExpired())
                return null;
            return value;
        }

        public boolean isExpired() {
            return Objects.nonNull(this.getExpiryAt())
                    && System.currentTimeMillis() > this.getExpiryAt();
        }
    }

    private final ConcurrentHashMap<K, CachedEntry<V>> cache = new ConcurrentHashMap<>();

    @Override
    public void put(K key, V value, long timeToLive) {
        CachedEntry cachedEntry = CachedEntry.builder()
                .value(value)
                .expiryAt(System.currentTimeMillis() + timeToLive)
                .build();
        cache.put(key, cachedEntry);
    }

    @Override
    public void put(K key, V value) {
        CachedEntry cachedEntry = CachedEntry.builder()
                .value(value)
                .build();
        cache.put(key, cachedEntry);
    }

    @Override
    public V get(K key) {
        return Optional.ofNullable(cache.get(key))
                .map(CachedEntry::value)
                .orElse(null);
    }
}