package splitwise.entities.splits;

import splitwise.entities.User;

public class PercentSplit extends Split {
    private double percentage;
    public PercentSplit(final User user, final double percentage) {
        super(user);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
