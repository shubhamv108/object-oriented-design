package httpclient;

public class Params extends AbstractPairs {
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final Pair pair : this.pairs) {
            if (!builder.isEmpty())
                builder.append("&");
            builder.append(pair.getKey()).append('=').append(String.join(",", pair.getValues()));
        }
        if (!builder.isEmpty())
            builder.setCharAt(0, '?');
        return builder.toString();
    }
}
