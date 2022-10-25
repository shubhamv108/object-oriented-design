package tennis.game.implementation;

public interface ITennisGamePlayersScore {
    boolean receiverHasAdvantage();

    boolean serverHasAdvantage();

    boolean receiverHasWon();

    boolean serverHasWon();

    boolean isDeuce();

    int getServerScore();

    int getReceiverScore();
}
