package commons.observer;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Observable<Observable> {
    private Collection<IObserver<Observable>> observers = new CopyOnWriteArrayList<>();

    public void attachObserver(final IObserver<Observable> observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers() {
        this.observers.forEach(observer -> observer.notify((Observable) this));
    }
}
