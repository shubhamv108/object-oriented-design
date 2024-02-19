package stock;

import org.checkerframework.checker.units.qual.A;
import ratelimiter.AbstractRateLimiter;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String name, double price);
}

class Stock {

    private String name;
    private double price;

    public Stock(final String name, final double price) {
        this.name = name;
        this.price = price;
    }

    public void update(final double price) {
        this.price = price;
    }

}

class StockMarket {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(final Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(final Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(final String name, final double price) {
        this.observers.forEach(observer -> observer.update(name, price));
    }

    public void setPrice(final String name, final double price) {
        this.notifyObservers(name, price);
    }
}
