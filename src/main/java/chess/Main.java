package chess;

import chess.entities.Game;
import chess.entities.User;
import chess.orchestrators.GameOrchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

/**
 * ToDo:
 *  Add unit test cases for rules validation
 *  Allow adding new users
 *  Add scoring for each user
 */
public class Main {
    public static void main(String[] args) throws IOException {
        final User whiteUser = User.builder()
                .withUserName("user_W")
                .withDisplayName("White User One")
                .build();
        final User blackUser = User.builder()
                .withUserName("user_B")
                .withDisplayName("Black user One")
                .build();

        final Game game = Game.builder()
                .withWhiteUser(whiteUser)
                .withBlackUser(blackUser)
                .build();

        final GameOrchestrator orchestrator = GameOrchestrator.builder()
                .withGame(game)
                .build();


        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        final String[] commands = GET_COMMANDS();
        int i = 0;

        while (game.isActive()) {
            System.out.println(game.toString());
            String input = reader.readLine(); // commands[i++];
            try {
                orchestrator.play(input);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        System.out.println(game.toString());
        System.out.println(game.getStatus());

        reader.close();
    }

    private static final String[] GET_COMMANDS() {
        return new String[] {
                "move e2 e3",
                "move a7 a6",
                "move d1 f3",
                "move b7 b6",
                "move f1 c4",
                "move a6 a5",
                "move f3 f7",
                "move a5 a4"
        };
    }
}
