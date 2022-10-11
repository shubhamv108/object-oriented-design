package weather;

public class StatisticsDisplay implements DisplayElement, Observer {

    private final WeatherData weatherData;

    public StatisticsDisplay(final WeatherData weatherData) {
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
