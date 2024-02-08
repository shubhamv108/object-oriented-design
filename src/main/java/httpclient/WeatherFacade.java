package httpclient;

import java.io.IOException;

public class WeatherFacade {

    public Object[] getWeather(final String city) throws IOException {
        var client = new WeatherServiceClient();
        return new Object[] {
                client.getTemperature(city),
                client.getLocation(city),
        };

    }

}
