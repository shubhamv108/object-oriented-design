package stock;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockOrderBookSystem {

    public enum OrderStatus {
        CREATION_STARTED, IN_PROGRESS, FAIL, COMPLETE, CANCEL_STARTED, CANCEL;
    }

    public enum Side {
        BUY, SELL
    }

    public class Order {
        private final String id;
        private final Double price;
        private final Side side;

        private final Lock lock = new ReentrantLock();

        private OrderStatus status = OrderStatus.CREATION_STARTED;

        public Order(String id, Double price, Side side) {
            this.id = id;
            this.price = price;
            this.side = side;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }
    }

    public class OrderBook {
        private final Map<String, Order> orders = new ConcurrentHashMap<>();
        private final Map<Side, ConcurrentSkipListMap<Double, Set<String>>> orderBooks;

        private final Map<Side, ConcurrentHashMap<Double, Lock>> priceLocks = Map.of(
                Side.BUY, new ConcurrentHashMap<>(),
                Side.SELL, new ConcurrentHashMap<>());

        public OrderBook() {
            orderBooks = Map.of(
                Side.BUY, new ConcurrentSkipListMap<>(Collections.reverseOrder()),
                Side.SELL, new ConcurrentSkipListMap<>());
        }

        private Lock getPriceLock(Side side, double price) {
            return priceLocks.get(side)
                    .computeIfAbsent(price, p -> new ReentrantLock());
        }

        public void addOrder(String id, double price, Side side) {
            Order order = new Order(id, price, side);
            Order existing = orders.putIfAbsent(id, order);
            if (existing != null && !OrderStatus.CREATION_STARTED.equals(existing.status))
                throw new IllegalStateException(String.format("orderAlreadyExistsWithId=%s", id));
            else if (existing != null)
                order = existing;

            order.lock.lock();
            try {
                if (!OrderStatus.CREATION_STARTED.equals(order.status))
                    throw new IllegalStateException(String.format("orderAlreadyExistsWithId=%s", id));

                Lock priceLock = getPriceLock(order.side, order.price);
                priceLock.lock();
                try {
                    orderBooks.get(order.side)
                        .computeIfAbsent(order.price, orders -> ConcurrentHashMap.newKeySet())
                        .add(id);
                } finally {
                    priceLock.unlock();
                }
                order.setStatus(OrderStatus.IN_PROGRESS);
            } catch (IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                order.setStatus(OrderStatus.FAIL);
                throw e;
            } finally {
                order.lock.unlock();
            }
        }

        public void cancelOrder(String id) {
            Order order = orders.get(id);
            if (order == null || !(OrderStatus.IN_PROGRESS.equals(order.status) || OrderStatus.CANCEL_STARTED.equals(order.status)))
                throw new IllegalArgumentException(String.format("orderNotFoundWithId=%s", id));

            order.lock.lock();
            try {
                if (!(OrderStatus.IN_PROGRESS.equals(order.status) || OrderStatus.CANCEL_STARTED.equals(order.status)))
                    throw new IllegalArgumentException(String.format("orderCannotBeCancelledWithId=%sAndStatus=%s", id, order.status));

                ConcurrentSkipListMap<Double, Set<String>> orderBook = orderBooks.get(order.side);
                order.setStatus(OrderStatus.CANCEL_STARTED);
                Lock priceLock = getPriceLock(order.side, order.price);
                priceLock.lock();
                try {
                    Set<String> orders = orderBook.get(order.price);
                    if (orders != null) {
                        orders.remove(order.id);
                        if (orders.isEmpty())
                            orderBook.remove(order.price);
                    }
                } finally {
                    priceLock.unlock();
                }
                order.setStatus(OrderStatus.CANCEL);
            } finally {
                order.lock.unlock();
            }
        }

        public Double bestBuy() {
            Map.Entry<Double, Set<String>> entry = orderBooks.get(Side.BUY).firstEntry();
            return Optional.ofNullable(entry).map(Map.Entry::getKey).orElse(null);
        }

        public Double bestSell() {
            Map.Entry<Double, Set<String>> entry = orderBooks.get(Side.SELL).firstEntry();
            return Optional.ofNullable(entry).map(Map.Entry::getKey).orElse(null);
        }
    }

}
