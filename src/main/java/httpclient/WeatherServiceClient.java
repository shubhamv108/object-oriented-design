package httpclient;

import java.io.IOException;

public class WeatherServiceClient {

    public Object getTemperature(final String city) throws IOException {
        HttpResponse response = new HttpClient()
                .send(new HttpRequestMessageBuilder()
                              .method("GET")
                              .uri(String.format("http://api.weatherapi.com/v1?q=%s&key=cb9266bd7c414f0f88452150240802", city))
                              .accept("application/json")
                              .connectionTimeoutInMilliSeconds(2000)
                              .readTimeoutInMilliSeconds(10000)
                              .build());
        System.out.println(response);
        return response.getBody();
    }

    public Object getLocation(final String city) throws IOException {
        HttpResponse response = new HttpClient()
                .send(new HttpRequestMessageBuilder()
                              .method("GET")
                              .uri(String.format("http://api.weatherapi.com/v1?q=%s&key=cb9266bd7c414f0f88452150240802", city))
                              .accept("application/json")
                              .connectionTimeoutInMilliSeconds(2000)
                              .readTimeoutInMilliSeconds(10000)
                              .build());
        System.out.println(response);
        return response.getBody();
    }

}
