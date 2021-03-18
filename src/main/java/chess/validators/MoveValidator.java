package chess.validators;

import chess.entities.board.Move;
import commons.exceptions.GameException;
import commons.validators.AbstractValidator;

public class MoveValidator extends AbstractValidator<Move> {

    @Override
    public MoveValidator validate(final Move object) {
        return this;
    }

    @Override
    public MoveValidator validateOrThrowException(final Move object) throws GameException {
        return this;
    }

}
