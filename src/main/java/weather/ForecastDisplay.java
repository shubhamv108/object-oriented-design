package weather;

public class ForecastDisplay implements Observer, DisplayElement {

    private final WeatherData weatherData;

    public ForecastDisplay(final WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void update(final float temperature,
                       final float humidity,
                       final float pressure) {
        display();
    }

    @Override
    public void display() {

    }
}
