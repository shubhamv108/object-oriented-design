package hashtable;

import java.util.Iterator;
import java.util.LinkedList;

public class HashTable<K, V> {

    static class Item<K, V> {

        private Item(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        private K key;
        private V value;
    }

    private LinkedList<Item>[] table;
    private int tableSize;
    public HashTable(final int tableSize) {
        this.tableSize = tableSize;
        this.table = new LinkedList[this.tableSize];
    }

    private int hashFunction(final K key) {
        return key.hashCode() % this.tableSize;
    }

    public void put(final K key, final V value) {
        final int hashIndex = this.hashFunction(key);
        if (this.table[hashIndex] == null)
            this.table[hashIndex] = new LinkedList<>();

        for (Item item : this.table[hashIndex])
            if (item.key.equals(key)) {
                item.value = value;
                return;
            }
        this.table[hashIndex].add(new Item(key, value));
    }

    public V get(final K key) {
        final int hashIndex = this.hashFunction(key);
        for (final var item : this.table[hashIndex])
            if (item.key.equals(key))
                return (V) item.value;

        return null;
    }

    public void remove(final K key) {
        final int hashIndex = this.hashFunction(key);
        this.table[hashIndex].remove();
        for (final Iterator<Item> it = this.table[hashIndex].iterator(); it.hasNext(); ) {
            final var item = it.next();
            if (item.key.equals(key))
                it.remove();
        }
    }

    public static void main(String[] args) {
        final var hashTable = new HashTable<>(10);
        hashTable.put(1, 0);
        System.out.println(hashTable.get(1));
        hashTable.remove(1);
        System.out.println(hashTable.get(1));
    }
}
