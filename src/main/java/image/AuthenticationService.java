package image;

public class AuthenticationService {

    public static AuthenticationService getService() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final AuthenticationService INSTANCE = new AuthenticationService();
    }

    public boolean isUserAuthenticated(final String userName, final String password) {
        return true;
    }

}
