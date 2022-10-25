package dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OwnerInvocationHandler implements InvocationHandler {
    Person person;

    public OwnerInvocationHandler(Person person) {
        this.person = person;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws IllegalAccessException {
        try {
            if (method.getName().startsWith("get"))
                return method.invoke(person, args);
            else if (method.getName().equals("setGeekRating"))
                throw new IllegalAccessException();
            else if (method.getName().startsWith("set"))
                return method.invoke(person, args);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
