package vendingmachine.enums;

public enum Coin {

    ONE(1), TWO(2), FIVE(5), TEN(10);

    private int val;

    Coin(final int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }

    @Override
    public String toString() {
        return String.valueOf(this.val);
    }
}
