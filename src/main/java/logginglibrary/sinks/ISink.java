package logginglibrary.sinks;

import logginglibrary.api.Message;

import java.io.IOException;

public interface ISink {

    int getId();
    void writeMessage(Message message);

}
