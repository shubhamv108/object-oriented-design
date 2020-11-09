package bowlingalley.lane.roll.implementations;

import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.roll.IRoll;
import bowlingalley.lane.set.ISet;

import java.util.Optional;

public class Roll implements IRoll {

    private final int droppedPins;
    private final IPlayer player;
    private ISet set;

    public Roll(final Integer droppedPins, final IPlayer player) {
        this.droppedPins = Optional.ofNullable(droppedPins).orElse(0);
        this.player = player;
    }

    @Override
    public void setSet(final ISet set) {
        this.set = set;
    }

    @Override
    public Integer getDroppedPins() {
        return this.droppedPins;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "Dropped Pins=" + this.getDroppedPins() + ", Player=" + this.player;
    }

    public static Roll.RollBuilder builder() {
        return new Roll.RollBuilder();
    }

    public static class RollBuilder {
        private int droppedPins;
        private IPlayer player;

        public RollBuilder withDroppedPins(int droppedPins) {
            this.droppedPins = droppedPins;
            return this;
        }

        public RollBuilder withPlayer(IPlayer player) {
            this.player = player;
            return this;
        }

        public Roll build() {
            return new Roll(this.droppedPins, this.player);
        }
    }

}
