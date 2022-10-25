package dynamicproxy;

import java.lang.reflect.Proxy;

public class MatchMakingTestDrive {

    public static void main(String[] args) {
        MatchMakingTestDrive test = new MatchMakingTestDrive();
        test.drive();
    }

    public void drive() {
        Person joe = new PersonImpl("Joe");
        Person ownerProxy = this.getOwnerProxy(joe);
        System.out.println("Name: "+ ownerProxy.getName());
        try {
            ownerProxy.setGeekRating(10);
        } catch (Exception exception) {
            System.out.println("Can't set rating from owner proxy");
        }
        System.out.println("GeekRating: " + ownerProxy.getGeekRating());


        joe = new PersonImpl("Joe");
        Person nonOwnerProxy = this.getNonOwnerProxy(joe);
        System.out.println("Name: "+ nonOwnerProxy.getName());
        try {
            nonOwnerProxy.setGeekRating(10);
        } catch (Exception exception) {
            System.out.println("Can't set rating from non-owner proxy");
        }
        System.out.println("GeekRating: " + nonOwnerProxy.getGeekRating());
    }

    Person getOwnerProxy(Person person) {
        return (Person) Proxy.newProxyInstance(
                person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                new OwnerInvocationHandler(person));
    }

    Person getNonOwnerProxy(Person person) {
        return (Person) Proxy.newProxyInstance(
                person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                new NonOwnerInvocationHandler(person));
    }
}
