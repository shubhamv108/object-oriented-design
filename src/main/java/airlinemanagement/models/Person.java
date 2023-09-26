package airlinemanagement.models;

public abstract class Person {
    private String name;
    private String address;
    private String email;
    private String phone;

    private Account account;

    public Person(final Account account) {
        this.account = account;
    }

    public String getEmail() {
        return account.getEmail();
    }
}
