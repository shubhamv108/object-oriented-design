package tictactoe;

public enum Player {
    X('X'), O('O');

    private Character symbol;

    Player (Character symbol) {
        this.symbol = symbol;
    }

    public Character getSymbol() {
        return symbol;
    }
}
