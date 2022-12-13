package twitter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TweetSorterFactory {

    private static final Comparator<Tweet> oldPrioritySortter = (t1, t2) -> t1.isBefore(t2) ? -1 : 1;

    private static final Comparator<Tweet> newPrioritySortter = (t1, t2) -> t1.isBefore(t2) ? 1 : -1;

    private static final Map<SortOrder, Comparator<Tweet>> sortMap = new HashMap<>();

    static {
        sortMap.put(SortOrder.OLDEST, oldPrioritySortter);
        sortMap.put(SortOrder.NEWEST, newPrioritySortter);

    }

    public static Comparator<Tweet> get(SortOrder sort){
        return sortMap.get(sort);
    }
}
