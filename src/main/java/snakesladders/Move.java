package snakesladders;

public class Move {
    private User user;
    private int steps;
    public Move(User user, int steps) {
        this.user = user;
        this.steps = steps;
    }
    public User getUser() {
        return user;
    }
    public int getSteps() {
        return steps;
    }
}
