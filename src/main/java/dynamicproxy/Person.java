package dynamicproxy;

import java.lang.reflect.Proxy;

public interface Person {
    String getName();
    int getGeekRating();
    void setName(String name);
    void setGeekRating(int rating);
}
