package dynamicproxy;

public class PersonImpl implements Person {
    private String name;
    private int geekRating;

    public PersonImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getGeekRating() {
        return geekRating;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setGeekRating(int rating) {
        this.geekRating = rating;
    }
}
