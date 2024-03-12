package usernamealreadytaken;

public interface IUsernameAlreadyExistsValidator {
    Boolean exists(String userName);

    void add(String userName);
}
