package splitwise.entities.expenditures;

import splitwise.entities.splits.Split;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Expenditures {

    private final Map<Integer, Expenditure<Split>> expenditures;
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Expenditures() {
        this.expenditures = new HashMap<>();
    }

    public Expenditure<Split> create(Expenditure<Split> expenditure) {
        expenditure.setId(idGenerator.getAndIncrement());
        this.expenditures.put(expenditure.getId(), expenditure);
        return expenditure;
    }

}
