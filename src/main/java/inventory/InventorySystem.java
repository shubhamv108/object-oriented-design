package inventory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Core Entities
 *
 * 1, Store
 * 2. ItemInventory
 * 3. InventoryService
 */
public class InventorySystem {

    public class Store {

        private final String storeId;

        private final Map<String, ItemInventory> inventory =
                new ConcurrentHashMap<>();

        public Store(String storeId) {
            this.storeId = storeId;
        }

        public void addInventory(
                String skuId,
                int quantity) {

            inventory.compute(
                    skuId,
                    (k, existing) -> {

                        if (existing == null)
                            return new ItemInventory(
                                    skuId,
                                    quantity);

                        existing.release(quantity);
                        return existing;
                    });
        }

        public boolean reserve(
                String skuId,
                int quantity) {

            ItemInventory item =
                    inventory.get(skuId);

            return item != null &&
                    item.reserve(quantity);
        }

        public void release(
                String skuId,
                int quantity) {

            ItemInventory item =
                    inventory.get(skuId);

            if (item == null)
                throw new IllegalArgumentException();

            item.release(quantity);
        }

        public int available(String skuId) {

            ItemInventory item =
                    inventory.get(skuId);

            return item == null
                    ? 0
                    : item.getAvailable();
        }
    }

    public class ItemInventory {
        private final String skuId;
        private final AtomicInteger available;

        public ItemInventory(
                String skuId,
                int initialQuantity) {

            if (initialQuantity < 0)
                throw new IllegalArgumentException();

            this.skuId = skuId;
            this.available = new AtomicInteger(initialQuantity);
        }

        public String getSkuId() {
            return skuId;
        }

        public int getAvailable() {
            return available.get();
        }

        public boolean reserve(int quantity) {
            if (quantity <= 0)
                throw new IllegalArgumentException();

            while (true) {
                int current = available.get();

                if (current < quantity)
                    return false;
                if (available.compareAndSet(current, current - quantity))
                    return true;
            }
        }

        public void release(int quantity) {
            if (quantity <= 0)
                throw new IllegalArgumentException();

            available.addAndGet(quantity);
        }
    }

    public class InventoryService {

        private final Map<String, Store> stores = new ConcurrentHashMap<>();

        public void addInventory(String storeId, String skuId, int quantity) {
            stores.computeIfAbsent(storeId, Store::new)
                    .addInventory(skuId, quantity);
        }

        public boolean reserve(String storeId, String skuId, int quantity) {
            Store store = stores.get(storeId);
            return store != null && store.reserve(skuId, quantity);
        }

        public void release(String storeId, String skuId, int quantity) {
            Store store = Optional.ofNullable(stores.get(storeId))
                    .orElseThrow(IllegalArgumentException::new);

            store.release(skuId, quantity);
        }

        public int available(String storeId, String skuId) {
            return Optional.ofNullable(stores.get(storeId))
                    .map(store -> store.available(skuId))
                    .orElse(0);
        }
    }

}
