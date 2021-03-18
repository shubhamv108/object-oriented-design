package chess.entities;

import commons.IBuilder;

public class Player {
    private User user;

    private Player(final User user) {
        this.user = user;
    }

    public static PlayerBuilder builder() {
        return new PlayerBuilder();
    }

    private static class PlayerBuilder implements IBuilder<Player> {
        private User user;

        public PlayerBuilder withUser(final User user) {
            this.user = user;
            return this;
        }

        @Override
        public Player build() {
            return new Player(this.user);
        }
    }

}
