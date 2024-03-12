package usernamealreadytaken;

public interface IUserService {

    boolean isUsernameTaken(String username);
    void add(String username);

}
