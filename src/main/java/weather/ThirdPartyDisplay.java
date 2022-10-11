package weather;

public class ThirdPartyDisplay implements Observer, DisplayElement {

    private final WeatherData weatherData;

    public ThirdPartyDisplay(final WeatherData weatherData) {
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
