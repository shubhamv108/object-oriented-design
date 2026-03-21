package loadbalancer.strategies.consistenthashing;

import loadbalancer.models.Server;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConsistentHashing {

    // Sorted ring: hash -> node
    private final ConcurrentSkipListMap<Long, Server> ring = new ConcurrentSkipListMap<>();

    // Physical nodes
    private final Set<Server> servers = ConcurrentHashMap.newKeySet();

    private final int virtualNodes;

    public ConsistentHashing(final int virtualNodes) {
        this.virtualNodes = virtualNodes;
    }

    public ConsistentHashing(final int virtualNodes, final Collection<Server> servers) {
        this.virtualNodes = virtualNodes;
        servers.forEach(this::addServer);
    }

    public synchronized void addServer(final Server server) {
        if (servers.contains(server))
            return;

        servers.add(server);
        for (int i = 0; i < virtualNodes; ++i) {
            long hash = hash(server + "#" + i);
            ring.put(hash, server);
        }
    }

    public synchronized void removeServer(final Server server) {
        if (!servers.contains(server))
            return;

        servers.remove(server);
        for (int i = 0; i < virtualNodes; ++i) {
            long hash = hash(server + "#" + i);
            ring.remove(hash);
        }
    }

    public Server getServer(final String key) {
        if (ring.isEmpty()) {
            throw new IllegalStateException("No nodes available");
        }

        long hash = hash(key);

        Map.Entry<Long, Server> entry = ring.ceilingEntry(hash);

        if (entry == null) {
            return ring.firstEntry().getValue();
        }

        return entry.getValue();
    }

    private long hash(final String key) {
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        return murmurHash64(data);
    }

    private long murmurHash64(final byte[] data) {
        int length = data.length;
        int seed = 0xe17a1465;

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (length * m);

        int length8 = length / 8;

        for (int i = 0; i < length8; i++) {
            int i8 = i * 8;
            long k = ((long) data[i8 + 0] & 0xff)
                    | (((long) data[i8 + 1] & 0xff) << 8)
                    | (((long) data[i8 + 2] & 0xff) << 16)
                    | (((long) data[i8 + 3] & 0xff) << 24)
                    | (((long) data[i8 + 4] & 0xff) << 32)
                    | (((long) data[i8 + 5] & 0xff) << 40)
                    | (((long) data[i8 + 6] & 0xff) << 48)
                    | (((long) data[i8 + 7] & 0xff) << 56);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        int remaining = length % 8;
        int offset = length8 * 8;

        switch (remaining) {
            case 7: h ^= (long) (data[offset + 6] & 0xff) << 48;
            case 6: h ^= (long) (data[offset + 5] & 0xff) << 40;
            case 5: h ^= (long) (data[offset + 4] & 0xff) << 32;
            case 4: h ^= (long) (data[offset + 3] & 0xff) << 24;
            case 3: h ^= (long) (data[offset + 2] & 0xff) << 16;
            case 2: h ^= (long) (data[offset + 1] & 0xff) << 8;
            case 1: h ^= (long) (data[offset] & 0xff);
                h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }

    public void printRing() {
        ring.forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
