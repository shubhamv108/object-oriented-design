package remotecontrol;

public interface Command {
    void execute();
    void undo();

//    void store();
//    void load();
}
