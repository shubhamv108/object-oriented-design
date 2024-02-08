package httpclient;

import java.util.List;
import java.util.Map;

public class HttpResponse {
    private final int responseCode;
    private final Object body;
    private final Map<String, List<String>> headers;

    public HttpResponse(
            final int responseCode,
            final Object body,
            final Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.body = body;
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "responseCode=" + responseCode +
                ", body=" + body +
                ", headers=" + headers +
                '}';
    }
}
