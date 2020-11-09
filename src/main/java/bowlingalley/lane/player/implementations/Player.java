package bowlingalley.lane.player.implementations;

import bowlingalley.lane.game.implementation.IGame;
import bowlingalley.lane.player.IPlayer;
import bowlingalley.user.User;

import java.util.Objects;

public class Player implements IPlayer {

    private int spares = 0;
    private int strikes = 0;
    private User user;
    private boolean isWinner;
    private final IGame game;

    public Player(final User user, final IGame game) {
        this.user = user;
        this.game = game;
    }

    @Override
    public int getScore() {
        return (this.spares * 5) + (this.strikes * 10);
    }

    @Override
    public void incSpares() {
        this.spares++;
    }

    @Override
    public void incStrikes() {
        this.strikes++;
    }

    @Override
    public Integer getSpares() {
        return this.spares;
    }

    @Override
    public Integer getStrikes() {
        return this.strikes;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public boolean setWinner(boolean winner) {
        return this.isWinner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(user, player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return this.user.toString();
    }

    public static PlayerBuilder builder() {
        return new PlayerBuilder();
    }

    public static class PlayerBuilder {
        private User user;
        private IGame game;

        public PlayerBuilder withUser(final User user) {
            this.user = user;
            return this;
        }

        public PlayerBuilder withGame(final IGame game) {
            this.game = game;
            return this;
        }

        public Player build() {
            return new Player(this.user, this.game);
        }
    }

}
