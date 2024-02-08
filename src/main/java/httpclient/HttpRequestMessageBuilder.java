package httpclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;

public class HttpRequestMessageBuilder {
    private String method = "GET";
    private String uri;
    private Params params;
    private String body;
    private Headers headers;

    private boolean isSecure;

    private int connectionTimeoutInMilliseconds;
    private int readTimeoutInMilliseconds;

    private boolean followRedirects = true;

    private Long contentLength;

    private String contentType;
    private String accept;

    private String authorization;

    private String bearer;

    public HttpRequestMessageBuilder method(final String method) {
        this.method = method;
        return this;
    }

    public HttpRequestMessageBuilder uri(final String uri) {
        this.uri = uri;
        return this;
    }

    public HttpRequestMessageBuilder params(final Params params) {
        this.params = params;
        return this;
    }

    public HttpRequestMessageBuilder body(final String body) {
        this.body = body;
        return this;
    }

    public HttpRequestMessageBuilder headers(final Headers headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequestMessageBuilder isSecure() {
        this.isSecure = true;
        return this;
    }

    public HttpRequestMessageBuilder connectionTimeoutInMilliSeconds(final int millis) {
        this.connectionTimeoutInMilliseconds = millis;
        return this;
    }

    public HttpRequestMessageBuilder readTimeoutInMilliSeconds(final int millis) {
        this.readTimeoutInMilliseconds = millis;
        return this;
    }

    public HttpRequestMessageBuilder contentLength(final Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public HttpRequestMessageBuilder contentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpRequestMessageBuilder accept(final String accept) {
        this.accept = accept;
        return this;
    }

    public HttpRequestMessageBuilder authorization(final String authorization) {
        this.authorization = authorization;
        return this;
    }

    public HttpRequestMessageBuilder bearer(final String bearer) {
        this.bearer = bearer;
        return this;
    }

    public HttpURLConnection build() throws IOException {
        final HttpURLConnection con = (HttpURLConnection)
                new URL(this.uri + Optional.ofNullable(this.params).map(Params::toString).orElse("")).openConnection();
        con.setRequestMethod(this.method);

        if (this.headers != null) {
            final Iterator<Pair> headerIterator = this.headers.iterator();
            while (headerIterator.hasNext()) {
                final Pair pair = headerIterator.next();
                con.setRequestProperty(pair.getKey(), String.join(",", pair.getValues()));
            }
        }

        if (this.contentLength != null)
            con.setRequestProperty("Content-Length", this.contentLength.toString());

        if (this.contentType != null)
            con.setRequestProperty("Content-Type", this.contentType);

        if (this.accept != null)
            con.setRequestProperty("Accept", this.accept);

        if (this.authorization != null)
            con.setRequestProperty("Authorization", this.authorization);

        if (this.bearer != null)
            con.setRequestProperty("Authorization", "Bearer" + this.bearer);

        con.setConnectTimeout(this.connectionTimeoutInMilliseconds);
        con.setReadTimeout(this.readTimeoutInMilliseconds);
        con.setInstanceFollowRedirects(this.followRedirects);

        if (this.contentType != null)
            con.setFixedLengthStreamingMode(this.contentLength);
        return con;
    }
}
