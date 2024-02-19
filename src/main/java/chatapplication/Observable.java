package chatapplication;

import java.util.HashSet;
import java.util.Set;

public abstract class Observable<Observer extends IObserver, Notification> {

    protected final Set<Observer> observers = new HashSet<>();

    public void addObserver(final Observer observer) {
        this.observers.add(observer);
    }
    public void removeObserver(final Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(final Notification notification) {
        this.observers.stream()
                .filter(observer -> this.filter(observer, notification))
                .forEach(observer -> observer.notifyObserver(notification));
    }

    protected abstract boolean filter(Observer observer, Notification notification);

}
