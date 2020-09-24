package tictactoe;

public class Move {

    private Integer x;
    private Integer y;
    private User user;

    public Move(User user, int x, int y) {
        this.user = user;
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public User getUser() {
        return user;
    }
}
