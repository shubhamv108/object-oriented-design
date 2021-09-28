package chess2;

import chess2.board.Board;
import chess2.board.CellPosition;
import chess2.board.pieces.Piece;

import java.util.List;

public class Game {

    Board board;
    Player[] players;
    Player currentPlayer;
    List<Move> moves;
    GameStatus status;

    public boolean playerMove(CellPosition from, CellPosition to, Piece piece) {}
    public boolean endGame() {}
    private void changeTurn() {}

}
