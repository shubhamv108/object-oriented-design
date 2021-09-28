package amazon.actors;

import stockbrokerage.enums.AccountStatus;

import java.util.List;

public class Account {
    String name;
    String email;
    String phoneNumber;
    String userName;
    String password;

    List<Address> addresses;

    AccountStatus accountStatus;
}
