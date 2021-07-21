package multilevelcache.api;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheAvailableCapacityResponse {

    private final Map<String, Integer> availability = new LinkedHashMap<>();

    public void put(String cacheId, int availableCapacity) {
        this.availability.put(cacheId, availableCapacity);
    }

    @Override
    public String toString() {
        return "CacheAvailableCapacityResponse{" +
                "availability=" + this.availability +
                '}';
    }
}
