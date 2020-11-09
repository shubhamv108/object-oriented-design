package bowlingalley.models;

import bowlingalley.user.User;

public class RollByUser {

    private final int droppedPins;
    private final User user;

    public RollByUser(final int droppedPins, final User user) {
        this.droppedPins = droppedPins;
        this.user = user;
    }

    public int getDroppedPins() {
        return this.droppedPins;
    }

    public User getUser() {
        return this.user;
    }

    public static RollByUser.RollBuilder builder() {
        return new RollByUser.RollBuilder();
    }

    public static class RollBuilder {
        private int droppedPins;
        private User user;

        public RollBuilder withDroppedPins(final int droppedPins) {
            this.droppedPins = droppedPins;
            return this;
        }

        public RollBuilder withUser(final User user) {
            this.user = user;
            return this;
        }

        public RollByUser build() {
            return new RollByUser(this.droppedPins, this.user);
        }
    }

}
