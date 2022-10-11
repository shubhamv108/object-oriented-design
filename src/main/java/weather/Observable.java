package weather;

public interface Observable {
    boolean registerObserver(Observer observer);

    boolean removeObserver(Observer observer);

    void notifyObservers();

    <State> State getState();
    <State> void setState(State state);
}
