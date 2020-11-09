package bowlingalley.lane.set.implementations;

import bowlingalley.constants.enums.MaxRollForPlayerForSetStrategyType;
import bowlingalley.exceptions.GameException;
import bowlingalley.factories.stategyfactory.implementations.MaxRollForPlayerForSetStrategyFactory;
import bowlingalley.lane.game.implementation.IGame;
import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.roll.IRoll;
import bowlingalley.lane.set.ISet;
import bowlingalley.strategies.IStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Set implements ISet {

    private final Map<IPlayer, List<IRoll>> playerRolls = new HashMap<>();
    private final List<IRoll> rolls = new ArrayList<>();
    private final Integer maxRollCountForSet;
    private final Integer countOfPinsInSet;
    private final MaxRollForPlayerForSetStrategyType maxRollForPlayerForSetStrategyType;
    private final IGame game;

    private transient IStrategy<IPlayer, Integer> maxRollForPlayerPerSetStrategy;

    public Set(final Integer maxRollCountForSet, final Integer countOfPinsInSet,
               final MaxRollForPlayerForSetStrategyType maxRollForPlayerForSetStrategyType, final IGame game) {
        this.maxRollCountForSet = maxRollCountForSet;
        this.countOfPinsInSet = countOfPinsInSet;
        this.maxRollForPlayerForSetStrategyType = maxRollForPlayerForSetStrategyType;
        this.maxRollForPlayerPerSetStrategy = MaxRollForPlayerForSetStrategyFactory.getInstance().get(this);
        this.game = game;
    }

    @Override
    public void addRoll(final IRoll roll) {
        this.roll(roll);
        roll.setSet(this);
        List<IRoll> rolls = this.playerRolls.get(roll.getPlayer());
        if (rolls == null) this.playerRolls.put(roll.getPlayer(), rolls = new ArrayList<>());
        rolls.add(roll);
        this.rolls.add(roll);
    }

    private void roll(final IRoll roll) {
        int droppedPinsInSet = this.getDroppedPins(roll.getPlayer());
        int playerRollCount = this.getPlayerRollCount(roll.getPlayer());

        this.validateRoll(roll.getPlayer(), droppedPinsInSet, playerRollCount);

        int newDroppedPins = droppedPinsInSet + roll.getDroppedPins();
        if (newDroppedPins > this.getCountOfPinsInSet() || newDroppedPins < 0) {
            throw new GameException(String.format(
                    "Invalid dropped number of pins %s in roll by player %s", roll.getDroppedPins(), roll.getPlayer()));
        }

        if (newDroppedPins == this.countOfPinsInSet) {
            if (playerRollCount == 0) roll.getPlayer().incStrikes();
            if (playerRollCount == 1) roll.getPlayer().incSpares();
        }

    }

    private int getPlayerRollCount(final IPlayer player) {
        return Optional.ofNullable(this.playerRolls.get(player)).map(List::size).orElse(0);
    }

    private Integer getDroppedPins(final IPlayer player) {
        return Optional.ofNullable(this.playerRolls.get(player))
                .map(rolls -> rolls.stream().map(IRoll::getDroppedPins).mapToInt(Integer::valueOf).sum())
                .orElse(0);
    }

    private void validateRoll(final IPlayer player, final int droppedPinsInSet, final int playerRollCount) {
        if (this.countOfPinsInSet == droppedPinsInSet)
            throw new GameException(String.format("Player %s has already dropped all pins in this set.", player));
        if (playerRollCount == this.maxRollForPlayerPerSetStrategy.apply(player))
            throw new GameException(String.format("Player %s has utilized all permitted rolls for current set.", player));
    }

    private boolean canPlayerRoll(final IPlayer player) {
        return this.countOfPinsInSet > this.getDroppedPins(player) &&
                this.getPlayerRollCount(player) < this.maxRollForPlayerPerSetStrategy.apply(player);
    }

    @Override
    public boolean isComplete() {
        return this.getGame().getPlayers().stream().filter(this::canPlayerRoll).count() == 0;
    }

    @Override
    public Integer getMaxRollCountForSet() {
        return this.maxRollCountForSet;
    }

    private IGame getGame() {
        return this.game;
    }

    private Integer getCountOfPinsInSet() {
        return this.countOfPinsInSet;
    }

    @Override
    public MaxRollForPlayerForSetStrategyType getMaxRollForPlayerForSetStrategyType() {
        return this.maxRollForPlayerForSetStrategyType;
    }

    public static Set.SetBuilder builder() {
        return new Set.SetBuilder();
    }

    public static class SetBuilder {
        private int maxRollCountForSet;
        private int countOfPinsInSet;
        private MaxRollForPlayerForSetStrategyType maxRollForPlayerForSetStrategyType;
        private IGame game;

        public Set.SetBuilder withMaxRollCountForSet(final int maxRollCountForSet) {
            this.maxRollCountForSet = maxRollCountForSet;
            return this;
        }

        public Set.SetBuilder withCountOfPinsInSet(final int countOfPinsInSet) {
            this.countOfPinsInSet = countOfPinsInSet;
            return this;
        }

        public Set.SetBuilder withGame(final IGame game) {
            this.game = game;
            return this;
        }

        public Set.SetBuilder withMaxRollForPlayerForSetStrategyType(
                final MaxRollForPlayerForSetStrategyType maxRollForPlayerForSetStrategyType) {
            this.maxRollForPlayerForSetStrategyType = maxRollForPlayerForSetStrategyType;
            return this;
        }

        public Set build() {
            return new Set(this.maxRollCountForSet, this.countOfPinsInSet, this.maxRollForPlayerForSetStrategyType, this.game);
        }
    }

}
