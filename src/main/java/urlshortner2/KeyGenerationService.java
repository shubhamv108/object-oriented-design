package urlshortner2;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class KeyGenerationService {

    private final Queue<String> generatedShortUrls = new LinkedBlockingQueue<>();
    private final Map<String, Boolean> usedKeys = new ConcurrentHashMap<>();

    public boolean offer(String shortUrl) {
        Boolean used = usedKeys.get(shortUrl);
        if (used != null)
            return false;
        synchronized (shortUrl) {
            used = usedKeys.get(shortUrl);
            if (used != null)
                return false;

            usedKeys.put(shortUrl, false);
            generatedShortUrls.add(shortUrl);
            return true;
        }
    }

    public String poll() {
        String shortUrl = this.generatedShortUrls.poll();
        this.usedKeys.put(shortUrl, true);
        return shortUrl;
    }

}
