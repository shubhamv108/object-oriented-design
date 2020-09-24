package caching;

import java.util.HashMap;
import java.util.Map;

public class LRU<K, V> {

    Node head = new Node();
    Node tail = new Node();
    Map<K, Node> nodeMap;
    int capacity;

    public LRU(int capacity) {
        this.capacity = capacity;
        nodeMap = new HashMap<>(capacity);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        V result = null;
        Node node = nodeMap.get(key);
        if (node != null) {
            remove(node);
            add(node);
            result = node.value;
        }
        return result;
    }

    public void put(K key, V value) {
        Node node = nodeMap.get(key);
        if (node != null) {
            node.value = value;
            remove(node);
            add(node);
        } else {
            if (nodeMap.size() == capacity) {
                nodeMap.remove(tail.prev.key);
                remove(tail.prev);
            }
            Node newNode = new Node(key, value);
            add(newNode);
            nodeMap.put(key, newNode);
        }
    }

    private void add(Node node) {
        Node headNext = head.next;
        head.next = node;
        node.prev = head;
        node.next = headNext;
        headNext.prev = node;
    }

    private void remove(Node node) {
        Node nextNode = node.next;
        Node prevNode = node.prev;
        nextNode.prev = prevNode;
        prevNode.next = nextNode;
    }

    class Node {
        K key;
        V value;
        Node prev;
        Node next;
        Node() {}
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + key + "," + value + "]";
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node temp = head;
        while (temp != null) {
            builder.append(temp.toString()).append("->");
            temp = temp.next;
        }
        builder.append("\n").append(nodeMap);
        return builder.toString();
    }

    public static void main(String[] args) {
        LRU<Integer, Integer> lru = new LRU<>(0);
        lru.put(1, 1);
        lru.put(2, 2);
        System.out.println(lru.get(1));
        lru.put(3, 3);
        System.out.println(lru.get(2));
        lru.put(4, 4);
        System.out.println(lru.get(1));
        System.out.println(lru.get(3));
        System.out.println(lru.get(4));
        System.out.println(lru);
    }

}
