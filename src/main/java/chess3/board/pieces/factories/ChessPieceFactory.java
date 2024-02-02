package chess3.board.pieces.factories;

import chess3.board.pieces.Ount;
import chess3.board.pieces.Pawn;
import chess3.board.pieces.King;
import chess3.board.pieces.Knight;
import chess3.board.pieces.Piece;
import chess3.board.pieces.PieceColor;
import chess3.board.pieces.PieceType;
import chess3.board.pieces.Queen;
import chess3.board.pieces.Rook;

public class ChessPieceFactory {
    public Piece createPiece(PieceType type, PieceColor color) {
        switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case ROOK -> new Rook(color);
            case KNGHT -> new Knight(color);
            case OUNT -> new Ount(color);
            case PAWN -> new Pawn(color);
        }
        throw new IllegalArgumentException("Piece type not supported");
    }
}
