package commons.observer;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Observable implements IObservable<Observable> {
    private Collection<IObserver<Observable>> observers = new ConcurrentSkipListSet<>();

    @Override
    public void attachObserver(final IObserver<Observable> observer) {
        this.observers.add(observer);
    }

    @Override
    public void detachObserver(final IObserver<Observable> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(observer -> observer.notify((Observable) this));
    }
}
