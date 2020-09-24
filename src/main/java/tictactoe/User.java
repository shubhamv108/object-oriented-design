package tictactoe;

import java.util.List;

public class User {
    private String userName;
    private String displayName;
    private Statistics statistics;

    // many to one (lazy)
    List<Game> games;

    public User(String userName, String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
