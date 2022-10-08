package tennis.game;

public interface ITennisGame {
    void wonPoint(String playerName);
    String getScore();

    boolean receiverHasAdvantage();

    boolean serverHasAdvantage();

    boolean receiverHasWon();

    boolean serverHasWon();

    boolean isDeuce();

    String getServer();

    String getReceiver();

    int getServerScore();

    int getReceiverScore();
}
