package chess3.factories;

import chess3.Player;
import chess3.board.Square;
import chess3.board.pieces.Piece;
import chess3.board.pieces.PieceColor;
import chess3.board.pieces.PieceType;
import chess3.board.pieces.factories.ChessPieceFactory;

public class ChessGameFactory {

    private final ChessPieceFactory pieceFactory = new ChessPieceFactory();

    public Piece createPiece(PieceType type, PieceColor color) {
        return this.pieceFactory.createPiece(type, color);
    }
    public Player createPlayer(PieceColor color, Square startSquare) {
        // complete implementation
        return new Player(color);
    }


}
