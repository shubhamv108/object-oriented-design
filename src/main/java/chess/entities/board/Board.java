package chess.entities.board;

import chess.entities.Game;
import chess.entities.board.enums.PieceColor;
import chess.entities.board.pieces.Bishop;
import chess.entities.board.pieces.King;
import chess.entities.board.pieces.Knight;
import chess.entities.board.pieces.Pawn;
import chess.entities.board.pieces.Queen;
import chess.entities.board.pieces.Rook;
import commons.IBuilder;

import java.util.Date;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * ToDo:
 * 1) Remove 8x8 limit on Squares on Board
 * 2) Remove limit of only White and Black pieces
 */
public class Board {

    private final Squares squares;
    private final Pieces white;
    private final Pieces black;
    private final Game game;

    private final Date creationTimestamp = new Date();

    private Board(final Game game) {
        this.game = game;
        this.squares = Squares.builder().withBoard(this).build();
        this.white = Pieces.builder().build();
        this.black = Pieces.builder().build();
        this.init();
    }

    public Board(final Squares squares, final Pieces white, final Pieces black, final Game game) {
        this.game = game;
        this.squares = squares;
        this.white = white;
        this.black = black;
    }

    public void init() {
        reset(true);
    }

    public void reset() {
        reset(false);
    }

    private void reset(boolean init) {
        IntStream.range(0, 8).forEach(column -> {
            if (column == 0) {
                this.getSquare(0, column).setPiece(init ? Rook.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(0));
                this.getSquare(7, column).setPiece(init ? Rook.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(0));
                this.getSquare(1, column).setPiece(init ? Pawn.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(1));
                this.getSquare(6, column).setPiece(init ? Pawn.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(1));
            }
            if (column == 7) {
                this.getSquare(0, column).setPiece(init ? Rook.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(column * 2));
                this.getSquare(7, column).setPiece(init ? Rook.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(column * 2));
            }
            if (column == 1 || column == 6) {
                this.getSquare(0, column).setPiece(init ? Knight.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(column * 2));
                this.getSquare(7, column).setPiece(init ? Knight.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(column * 2));
            }
            if (column == 2 || column == 5) {
                this.getSquare(0, column).setPiece(init ? Bishop.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(column * 2));
                this.getSquare(7, column).setPiece(init ? Bishop.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(column * 2));
            }
            if (column == 3) {
                this.getSquare(0, column).setPiece(init ? Queen.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(column * 2));
                this.getSquare(7, column).setPiece(init ? Queen.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(column * 2));
            }
            if (column == 4) {
                this.getSquare(0, column).setPiece(init ? King.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece(column * 2));
                this.getSquare(7, column).setPiece(init ? King.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece(column * 2));
            }
            if (column != 0) {
                this.getSquare(1, column).setPiece(init ? Pawn.builder().withPieceColor(PieceColor.WHITE).withBoard(this).build() : this.getWhite().getPiece((column * 2) + 1));
                this.getSquare(6, column).setPiece(init ? Pawn.builder().withPieceColor(PieceColor.BLACK).withBoard(this).build() : this.getBlack().getPiece((column * 2) + 1));
            }
            if (init) {
                this.getWhite().add(this.getSquare(0, column).getPiece());
                this.getBlack().add(this.getSquare(7, column).getPiece());
                this.getWhite().add(this.getSquare(1, column).getPiece());
                this.getBlack().add(this.getSquare(6, column).getPiece());
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("  ");
        IntStream.rangeClosed(97, 104).forEach(column -> result.append((char) column).append(" "));
        result.append('\n');
        IntStream.iterate(7, e -> e >= 0, e -> e - 1).forEach(row -> {
            result.append(row+1).append(" ");
            IntStream.range(0, 8).forEach(column -> result.append(this.getSquare(row, column).toPieceView()).append(' '));
            result.append(row+1).append('\n');
        });
        result.append("  ");
        IntStream.rangeClosed(97, 104).forEach(column -> result.append((char) column).append(" "));
        return result.toString();
    }

    public boolean hasInsufficientMatingMaterial() {
        Iterator<Piece> pieces = this.getWhite().iterator();
        Iterator<Piece> blackPieces = this.getBlack().iterator();
        while (pieces.hasNext() || (pieces = blackPieces).hasNext()) {
            Piece piece = pieces.next();
            if (!piece.getClass().isAssignableFrom(King.class) && !piece.isKilled()) {
                return false;
            }
        }
        return true;
    }

    public Square getSquare(int row, int column) {
        if (row < 0 || row > 7 || column < 0 || column > 7) return null;
        return this.getSquares().getSquare(row, column);
    }

    public Square getSquare(final String square) {
        return this.getSquares().getSquare(square);
    }

    public Squares getSquares() {
        return this.squares;
    }

    public Pieces getPieces(final PieceColor color) {
        return PieceColor.WHITE.equals(color) ? this.getWhite() : this.getBlack();
    }

    public Pieces getWhite() {
        return this.white;
    }

    public Pieces getBlack() {
        return this.black;
    }

    public Piece move(final Move move) {
        Piece capturedPiece = move.getFrom().getPiece().moveTo(move.getTo());
        if (capturedPiece != null) {
            capturedPiece.removeSquare();
        }
        return capturedPiece;
    }

    public static BoardBuilder builder() {
        return new BoardBuilder();
    }

    public static class BoardBuilder implements IBuilder<Board> {
        private Squares squares;
        private Pieces white;
        private Pieces black;
        private Game game;

        public BoardBuilder withSquares(final Squares squares) {
            this.squares = squares;
            return this;
        }

        public BoardBuilder withWhite(final Pieces white) {
            this.white = white;
            return this;
        }

        public BoardBuilder withBlack(final Pieces black) {
            this.black = black;
            return this;
        }

        public BoardBuilder withGame(final Game game) {
            this.game = game;
            return this;
        }

        @Override
        public Board build() {
            return new Board(this.game);
        }
    }

    public static void main(String[] args) {
        Game game = new Game(null, null);
        Board board = game.getBoard();
        System.out.println(board.toString());
        board.reset();
        System.out.println(board.toString());
        Move
        move = Move.builder().withFrom("b1").withTo("c3").withGame(game).withMoveNumber(1).build();
        board.move(move);
        System.out.println(board);
        move = Move.builder().withFrom("e7").withTo("e5").withGame(game).withMoveNumber(2).build();
        board.move(move);
        move = Move.builder().withFrom("d2").withTo("d4").withGame(game).withMoveNumber(3).build();
        board.move(move);
        move = Move.builder().withFrom("g8").withTo("f6").withGame(game).withMoveNumber(4).build();
        board.move(move);
        move = Move.builder().withFrom("c1").withTo("e3").withGame(game).withMoveNumber(5).build();
        board.move(move);
        move = Move.builder().withFrom("b8").withTo("c6").withGame(game).withMoveNumber(6).build();
        board.move(move);
        move = Move.builder().withFrom("d1").withTo("d2").withGame(game).withMoveNumber(7).build();
        board.move(move);
        move = Move.builder().withFrom("f8").withTo("e7").withGame(game).withMoveNumber(8).build();
        board.move(move);
        System.out.println(board.toString());
        move = Move.builder().withFrom("e1").withTo("c1").withGame(game).withMoveNumber(9).build();
        board.move(move);
        move = Move.builder().withFrom("e8").withTo("g8").withGame(game).withMoveNumber(10).build();
        board.move(move);
        System.out.println(board);
    }

}
