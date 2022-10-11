package weather;

public class WeatherStation {
    public static void main(String[] args) {
        final WeatherData weatherData = new WeatherData();

        final CurrentConditionDisplay currentDisplay = new CurrentConditionDisplay(weatherData);
        final StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        final ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 34.5f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 90, 29.2f);
    }
}
