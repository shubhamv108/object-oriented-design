package loadbalancer.models;

import java.util.Map;

public class Request {
    private final String key;
    private final Map<String, String> headers;
    private final Object body;

    public Request(String key, Map<String, String> headers, Object body) {
        this.key = key;
        this.headers = headers;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Request{" +
                "key='" + key + '\'' +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
