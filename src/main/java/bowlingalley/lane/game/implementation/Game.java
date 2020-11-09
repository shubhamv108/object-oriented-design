package bowlingalley.lane.game.implementation;

import bowlingalley.exceptions.GameException;
import bowlingalley.factories.IFactory;
import bowlingalley.factories.implementations.SetFactory;
import bowlingalley.lane.player.IPlayer;
import bowlingalley.lane.player.implementations.Player;
import bowlingalley.lane.roll.IRoll;
import bowlingalley.lane.roll.implementations.Roll;
import bowlingalley.lane.set.ISet;
import bowlingalley.models.RollByUser;
import bowlingalley.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game implements IGame {

    private final Collection<IPlayer> players;
    private final Map<User, IPlayer> playerUserIndex = new HashMap<>();
    private final Integer maxSetCount;
    private final Integer countOfPinsInEachSet;
    private List<IPlayer> winners;

    private final List<ISet> sets = new ArrayList<>();

    private transient ISet currentSet;
    private final transient IFactory<Void, ISet> setFactory;

    public Game(final Collection<User> users, final Integer maxSetCount, final Integer countOfPinsInEachSet) {
        this.players = users.stream().map(user -> {
            Player player = Player.builder().withUser(user).withGame(this).build();
            this.playerUserIndex.put(user, player);
            return player;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        this.maxSetCount = maxSetCount;
        this.countOfPinsInEachSet = countOfPinsInEachSet;
        this.setFactory = new SetFactory(this);
        this.currentSet = this.getSetFactory().get(null);
        this.sets.add(this.getCurrentSet());
    }

    @Override
    public List<User> roll(final RollByUser rollByUser) {

        IPlayer player = this.playerUserIndex.get(rollByUser.getUser());

        if (player == null) {
            throw new GameException("Roll done by Invalid User");
        }

        IRoll roll = Roll.builder()
                        .withDroppedPins(rollByUser.getDroppedPins())
                        .withPlayer(this.playerUserIndex.get(rollByUser.getUser()))
                    .build();

        return Optional.ofNullable(roll(roll))
                .map(winners -> winners.stream().map(IPlayer::getUser).collect(Collectors.toList()))
                .orElse(null);
    }

    private synchronized List<IPlayer> roll(final IRoll roll) {

        System.out.println(String.format("Received Roll: %s", roll));

        if (this.getWinners() != null) {
            throw new GameException(String.format("Game completed. Winner(s): %s", this.getWinners()));
        }

        this.getCurrentSet().addRoll(roll);

        if (this.getCurrentSet().isComplete()) {
            if (this.sets.size() == this.getMaxSetCount()) {
                int highestScore = this.players.stream()
                        .map(player -> player.getScore())
                        .mapToInt(Integer::valueOf)
                        .max().getAsInt();
                this.winners = this.players.stream()
                        .filter(player -> player.setWinner(player.getScore() == highestScore))
                        .collect(Collectors.toList());
            } else {
                this.currentSet = this.getSetFactory().get(null);
                this.sets.add(this.getCurrentSet());
            }
        }

        return this.getWinners();
    }

    private List<IPlayer> getWinners() {
        return this.winners;
    }

    private ISet getCurrentSet() {
        return this.currentSet;
    }

    private IFactory<Void, ISet> getSetFactory() {
        return this.setFactory;
    }

    @Override
    public Integer getSetsCount() {
        return this.sets.size();
    }

    @Override
    public Integer getMaxSetCount() {
        return this.maxSetCount;
    }

    @Override
    public Integer getCountOfPinsInEachSet() {
        return this.countOfPinsInEachSet;
    }

    @Override
    public Collection<IPlayer> getPlayers() {
        return this.players;
    }

    public static GameBuilder builder() {
        return new GameBuilder();
    }

    public static class GameBuilder {

        private java.util.Set<User> users;
        private int maxSetCount = 10;
        private int countOfPinsInEachSet = 10;

        public GameBuilder withUsers(final java.util.Set<User> users) {
            this.users = users;
            return this;
        }

        public GameBuilder withMaxSetCount(final int maxSetCount) {
            this.maxSetCount = maxSetCount;
            return this;
        }

        public GameBuilder withCountOfPinsInEachSet(final int countOfPinsInEachSet) {
            this.countOfPinsInEachSet = countOfPinsInEachSet;
            return this;
        }

        public Game build() {
            return new Game(this.users, this.maxSetCount, this.countOfPinsInEachSet);
        }

    }

}
