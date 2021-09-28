package chess2;

import chess.entities.board.Piece;
import chess2.board.CellPosition;

public class Move {
    Player player;
    CellPosition from;
    CellPosition to;
    Piece killedPiece;
}
