package chess.enums;

import chess.entities.board.enums.PieceColor;

public enum ChessGameCommand {

    MOVE {
        @Override
        public boolean isArgumentsCountValid(final String... arguments) {
            return arguments.length == 2;
        }

        @Override
        public boolean isArgumentValid(final String argument) {
            if (argument.length() == 2) {
                int a = argument.charAt(0);
                int b = argument.charAt(1);
                return a > 96 && a < 123 && b > 48 && b < 58;
            }
            return false;
        }
    },
    RESET,
    UNDO,
    REDO,
    RESIGN {
        @Override
        public boolean isArgumentsCountValid(final String... arguments) {
            return arguments.length == 1;
        }

        @Override
        public boolean isArgumentValid(final String argument) {
            try {
                PieceColor.valueOf(argument.toUpperCase());
            } catch (final IllegalArgumentException ex) {
                return false;
            }
            return true;
        }
    };

    public boolean isArgumentsCountValid(String... arguments) {
        return true;
    }

    public boolean isArgumentValid(final String argument) {
        return true;
    }

}
