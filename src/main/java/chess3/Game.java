package chess3;

import chess3.board.Board;
import chess3.board.Square;
import chess3.board.pieces.Piece;
import chess3.board.pieces.PieceColor;
import chess3.factories.ChessGameFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Deque;

public class Game {

    private Board board;
    private GameStatus status;

    private Deque<Player> players;

    public Game() throws IOException {
        this.status = GameStatus.IN_PROGRESS;
        this.board = new Board();
        ChessGameFactory chessGameFactory = new ChessGameFactory();
        players.offer(chessGameFactory.createPlayer(PieceColor.WHITE, new Square(0, 0)));
        players.offer(chessGameFactory.createPlayer(PieceColor.BLACK, new Square(15, 15)));
        play();
    }

    private void play() throws IOException {
        if (!GameStatus.IN_PROGRESS.equals(this.status)) {
            System.out.println("Status: " + this.status);
            if (GameStatus.CHECKMATE.equals(this.status))
                System.out.println("Winner: " + players.peekLast());
            return;
        }
        Move nextMove = getMoveFromCurrentPlayer();
        if (isValidMove(nextMove))
            makeMove(nextMove);
        checkCondition();
        switchTurn();
        play();
    }

    private void checkCondition() {
        if (isCheckMate())
            this.status = GameStatus.CHECKMATE;
        else if (isStaleMate())
            this.status = GameStatus.STALEMATE;
    }

    private Move getMoveFromCurrentPlayer() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] position = Arrays.stream(br.readLine().split("\\s"))
                .mapToInt(Integer::valueOf)
                .toArray();
        return new Move(
            board.getSquare(position[0], position[1]),
            board.getSquare(position[2], position[3])
        );
    }

    private boolean isValidMove(Move move) {
        Piece piece = move.getSource().getPiece();
        if (piece == null)
            return false;
        return piece.isValidMove(this.board, move.getSource(), move.getDestination()) &&
            !piece.isUnderAttack(move.getDestination(), null);
    }

    public void makeMove(Move move) {
        Piece piece = move.getSource().getPiece();
        move.getSource().setPiece(null);
        move.getDestination().setPiece(piece);
    }

    private boolean isCheckMate() {
        return false;
    }

    private boolean isStaleMate() {
        return false;
    }

    private void switchTurn() {
        players.offer(players.poll());
    }
}
