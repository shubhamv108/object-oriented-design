package chess.entities.board;

import chess.utils.SquareUtils;
import commons.builder.IBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class Squares {

    private final Map<String, Square> squares = new HashMap<>();
    private final Board board;

    private Squares(final Board board) {
        this.board = board;
        IntStream.range(0, 8).forEach(row ->
            IntStream.range(0, 8).forEach(column -> {
                Square square = Square.builder().withRow(row).withColumn(column).withBoard(this.getBoard()).build();
                this.getSquares().put(square.toString(), square);
            })
        );
    }

    public Square getSquare(final String square) {
        return Optional.ofNullable(this.getSquares().get(square)).orElseThrow(() -> new IllegalArgumentException("No such square"));
    }

    public Square getSquare(final int row, final int column) {
        String key = SquareUtils.getSquareName(row, column);
        return this.getSquares().get(key);
    }

    public Map<String, Square> getSquares() {
        return this.squares;
    }

    public Board getBoard() {
        return board;
    }

    public static SquaresBuilder builder() {
        return new SquaresBuilder();
    }

    public static class SquaresBuilder implements IBuilder<Squares> {
        private Board board;

        public SquaresBuilder withBoard(final Board board) {
            this.board = board;
            return this;
        }

        @Override
        public Squares build() {
            return new Squares(this.board);
        }
    }

}
