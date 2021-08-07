package chat.managers;

import chat.entities.User;

import java.util.List;

public interface IUserManager {

    User create(User user);
    User get(String id);
    User delete(User user);
    List<User> getByIds(List<String> ids);
    default List<User> getByMobileNumbers(List<String> mobileNumbers) {
        return this.getByIds(mobileNumbers);
    }
}
