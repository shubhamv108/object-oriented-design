package multilevelcache.evictionpolicies;

import commons.builder.IBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUEvictionPolicy<Key, Value> implements IEvictionPolicy<Key, Value> {

    private final Node head = new Node(null);
    private final Node tail = new Node(null);
    private final Map<Key, Node> keyNodeMapping = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    private LRUEvictionPolicy() {
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    private class Node {
        private final Key key;
        private Node prev;
        private Node next;
        private Node (final Key key) { this.key = key; }

        public Key getKey() {
            return key;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }
    }

    @Override
    public void access(final Key key) {
        try {
            this.lock.lock();
            Node node = this.keyNodeMapping.get(key);
            if (node == null) {
                this.keyNodeMapping.put(key, node = new Node(key));
            } else {
                this.remove(node);
            }
            if (this.head.next != node) {
                this.add(node);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public Key remove(final Key key) {
        try {
            this.lock.lock();
            Node node = this.keyNodeMapping.get(key);
            if (node != null) {
                this.remove(node);
                this.keyNodeMapping.remove(key);
            }
        } finally {
            this.lock.unlock();
        }
        return key;
    }

    @Override
    public Key evict() {
        Node toBeRemoved = null;
        try {
            this.lock.lock();
            toBeRemoved = this.tail.getPrev();
            this.remove(toBeRemoved);
            this.keyNodeMapping.remove(toBeRemoved.getKey());
        } finally {
            this.lock.unlock();
        }
        return toBeRemoved.getKey();
    }

    private Node add(final Node node) {
        Node next = this.head.next;
        node.prev = this.head;
        node.next = next;
        this.head.next = node;
        next.prev = node;
        return node;
    }

    private Key remove(final Node node) {
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        return node.key;
    }

    public static LRUEvictionPolicyBuilder builder() {
        return new LRUEvictionPolicyBuilder();
    }

    public static class LRUEvictionPolicyBuilder implements IBuilder<LRUEvictionPolicy> {
        @Override
        public LRUEvictionPolicy build() {
            return new LRUEvictionPolicy();
        }
    }
}
