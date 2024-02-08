package httpclient;

import java.io.IOException;

public class WeatherCityDriver {

    public static void main(String[] args) throws IOException {
        System.out.println(new WeatherFacade().getWeather("Paris"));
    }

}
