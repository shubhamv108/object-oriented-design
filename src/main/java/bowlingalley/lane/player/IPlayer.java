package bowlingalley.lane.player;

import bowlingalley.user.User;

public interface IPlayer {

    void incStrikes();
    void incSpares();
    int getScore();

    Integer getSpares();

    Integer getStrikes();

    User getUser();

    void setUser(User user);

    boolean setWinner(boolean winner);
}
