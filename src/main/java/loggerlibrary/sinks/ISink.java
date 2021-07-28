package loggerlibrary.sinks;

import loggerlibrary.api.Message;

public interface ISink {

    int getId();
    void writeMessage(Message message);

}
