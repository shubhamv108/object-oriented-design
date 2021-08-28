package commons.statemachines;

public abstract class OnOffStateMachine {
    private boolean isOn;

    public boolean on() {
        return this.isOn = true;
    }

    public boolean off() {
        this.isOn = false;
        return true;
    }

    public boolean isOn() {
        return this.isOn;
    }
}
