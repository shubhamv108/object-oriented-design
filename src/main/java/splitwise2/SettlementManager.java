package splitwise2;

import java.util.ArrayList;
import java.util.List;

public class SettlementManager {

    private List<Settlement> settlements;

    private SettlementManager() {
        this.settlements = new ArrayList<>();
    }

    public static SettlementManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final SettlementManager INSTANCE = new SettlementManager();
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    public void showSettlements() {
        settlements.forEach(System.out::println);
    }
}
