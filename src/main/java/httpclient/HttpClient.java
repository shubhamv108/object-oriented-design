package httpclient;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpClient {

    public HttpResponse send(final HttpURLConnection connection) throws IOException {
        return new HttpResponse(
                connection.getResponseCode(),
                connection.getContent(),
                connection.getHeaderFields()
        );
    }

}
