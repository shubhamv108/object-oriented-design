package bowlingalley;

import bowlingalley.lane.game.implementation.IGame;
import bowlingalley.user.User;
import bowlingalley.models.RollByUser;
import bowlingalley.registries.UserRegistry;
import bowlingalley.lane.game.implementation.Game;
import java.util.List;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class Main {

    public static void main(String[] args) {
        User userA = UserRegistry.getInstance().getExistingOrNewUser("A");
        User userB = UserRegistry.getInstance().getExistingOrNewUser("B");
        userB = UserRegistry.getInstance().getExistingOrNewUser("B");

        IGame game = Game.builder()
                        .withUsers(new LinkedHashSet<>(Arrays.asList(userA, userB)))
                        .withCountOfPinsInEachSet(10)
                        .withMaxSetCount(2)
                    .build();

        RollByUser roll = RollByUser.builder().withDroppedPins(5).withUser(userA).build();
        List<User> winners = game.roll(roll);

        RollByUser roll2 = RollByUser.builder().withDroppedPins(4).withUser(userA).build();
        winners = game.roll(roll2);

//        RollByUser roll3 = RollByUser.builder().withDroppedPins(1).withUser(userA).build();
//        winners = game.roll(roll3);

        RollByUser roll4 = RollByUser.builder().withDroppedPins(1).withUser(userB).build();
        winners = game.roll(roll4);

        RollByUser roll5 = RollByUser.builder().withDroppedPins(4).withUser(userB).build();
        winners = game.roll(roll5);

        RollByUser roll6 = RollByUser.builder().withDroppedPins(10).withUser(userB).build();
        winners = game.roll(roll6);

//        RollByUser roll7 = RollByUser.builder().withDroppedPins(10).withUser(userA).build();
//        winners = game.roll(roll7);

        RollByUser roll7 = RollByUser.builder().withDroppedPins(9).withUser(userA).build();
        winners = game.roll(roll7);

//        RollByUser roll8 = RollByUser.builder().withDroppedPins(5).withUser(userB).build();
//        winners = game.roll(roll8);

//        RollByUser roll8 = RollByUser.builder().withDroppedPins(5).withUser(userA).build();
//        winners = game.roll(roll8);

        RollByUser roll8 = RollByUser.builder().withDroppedPins(1).withUser(userA).build();
        winners = game.roll(roll8);

        System.out.println(winners);

    }

}
