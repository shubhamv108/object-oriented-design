package weather;

public class CurrentConditionDisplay implements Observer, DisplayElement {

    private float temperature;
    private float humidity;
    private float pressure;

    private final WeatherData weatherData;

    public CurrentConditionDisplay(final WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }


    @Override
    public void update(final float temperature,
                       final float humidity,
                       final float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println(String.format(
                "CurrentConditions: %sF degrees and humidity %s.", temperature, humidity));
    }
}
