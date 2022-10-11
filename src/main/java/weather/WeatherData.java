package weather;

import java.util.Collection;
import java.util.LinkedHashSet;

public class WeatherData implements Observable {

    private float temperature;
    private float humidity;
    private float pressure;
    private final Collection<Observer> observers = new LinkedHashSet<>();

    @Override
    public boolean registerObserver(Observer observer) {
        return this.observers.add(observer);
    }

    @Override
    public boolean removeObserver(Observer observer) {
        return this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(observer -> observer.update(temperature, humidity, pressure));
    }

    @Override
    public <State> State getState() {
        return null;
    }

    @Override
    public <State> void setState(State state) {

    }

    public String getTemperature() {
        return null;
    }

    public String getHumidity() {
        return null;
    }

    public String getPressure() {
        return null;
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
}
