package chat.repositories;

import chat.entities.User;
import java.util.List;

public interface IUserRepository {

    User save(User user);
    User fetchById(String id);
    User remove(User user);
    List<User> fetchByIds(List<String> ids);

}
