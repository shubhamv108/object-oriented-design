package snakesladdrs2;

public class SnakesAndLaddersDriver {
    public static void main(String[] args) {
        new SnakesAndLaddersDriver().orchestrate();
    }

    private void orchestrate() {
        final SnakesAndLadders snakesAndLadders = new SnakesAndLadders();

        final int m = 2, n = 2;
        final SnakesAndLadders.Cell[] cells = new SnakesAndLadders.Cell[] {
                snakesAndLadders.new Cell(1, snakesAndLadders.new Ladder(3)),
                snakesAndLadders.new Cell(2),
                snakesAndLadders.new Cell(3),
                snakesAndLadders.new Cell(4, snakesAndLadders.new Snake(2))
        };

        final SnakesAndLadders.Dice dice = snakesAndLadders.new NormalDice();

        final SnakesAndLadders.Player[] players = new SnakesAndLadders.Player[] {
                snakesAndLadders.new Player("A"), snakesAndLadders.new Player("B")
        };

        final SnakesAndLadders.Game game = snakesAndLadders.new Game(m, n, cells, dice, players);

        game.play(players[0]);
        game.play(players[1]);

        System.out.println(game.getWinner());
    }

}
