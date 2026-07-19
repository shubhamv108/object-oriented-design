package BloomFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BloomFilterLibrary {

    public interface HashFunction {
        int hash(String element, int seed, int bitArraySize);
    }
    public class MurmurHashFunction implements HashFunction {
        @Override
        public int hash(String element, int seed, int bitArraySize) {
            int h = seed;
            int len = element.length();

            // Process the string char by char, treating each 16-bit char as two 8-bit bytes
            for (int i = 0; i < len; i++) {
                char c = element.charAt(i);

                // Process the low byte
                byte b1 = (byte) (c & 0xFF);
                h ^= b1;
                h *= 0x5bd1e995;
                h ^= (h >>> 13);

                // Process the high byte
                byte b2 = (byte) ((c >>> 8) & 0xFF);
                h ^= b2;
                h *= 0x5bd1e995;
                h ^= (h >>> 13);
            }

            h ^= (h >>> 16);
            h *= 0x85ebca6b;
            h ^= (h >>> 13);

            return Math.floorMod(h, bitArraySize);
        }
    }
    public class FNVHashFunction implements HashFunction {
        private static final int FNV_OFFSET_BASIS = 0x811c9dc5;
        private static final int FNV_PRIME = 0x01000193;

        @Override
        public int hash(String element, int seed, int bitArraySize) {
            int n = element.length();
            int hash = FNV_OFFSET_BASIS ^ seed;

            for (int i = 0; i < n; ++i) {
                hash ^= element.charAt(i);
                hash *= FNV_PRIME;
            }

            return Math.floorMod(hash, bitArraySize);
        }
    }
    public class DJB2HashFunction implements HashFunction {
        @Override
        public int hash(String element, int seed, int bitArraySize) {
            int hash = 5381 + seed;

            for (int i = 0; i < element.length(); i++) {
                hash = ((hash << 5) + hash) + element.charAt(i); // hash * 33 + c
            }

            return Math.floorMod(hash, bitArraySize);
        }
    }

    public class AtomicBitArray {
        private final int size;
        private final AtomicLongArray words;

        public AtomicBitArray(int bits) {
            this.size = bits;
            int wordCount = (bits + Long.SIZE - 1) / Long.SIZE;
            this.words = new AtomicLongArray(wordCount);
        }

        public void set(int bitIndex) {
            checkIndex(bitIndex);

            int wordIndex = bitIndex >>> 6;      // divide by 64
            long mask = 1L << (bitIndex & 63);   // mod 64

            while (true) {
                long current = words.get(wordIndex);
                long updated = current | mask;

                // Already set
                if (current == updated)
                    return;

                if (words.compareAndSet(wordIndex, current, updated))
                    return;
            }
        }

        public boolean get(int bitIndex) {
            checkIndex(bitIndex);

            int wordIndex = bitIndex >>> 6;
            long mask = 1L << (bitIndex & 63);

            return (words.get(wordIndex) & mask) != 0;
        }

        public void clear() {
            for (int i = 0; i < words.length(); ++i)
                words.set(i, 0L);
        }

        private void checkIndex(int index) {
            if (index < 0 || index >= size)
                throw new IndexOutOfBoundsException(index);
        }
    }

    public class BloomFilterConfig {
        private final int expectedElements;
        private final double falsePositiveRate;
        private final int bitArraySize;
        private final int numHashFunctions;

        public BloomFilterConfig(int expectedElements, double falsePositiveRate) {
            if (expectedElements <= 0 || falsePositiveRate <= 0 || falsePositiveRate >= 1)
                throw new IllegalArgumentException("expectedElements must be > 0 and falsePositiveRate must be between 0 and 1");
            this.expectedElements = expectedElements;
            this.falsePositiveRate = falsePositiveRate;
            // m = -(n * ln(p)) / (ln(2))^2
            this.bitArraySize = (int) Math.ceil(-(expectedElements * Math.log(falsePositiveRate)) / (Math.log(2) * Math.log(2)));
            // k = (m / n) * ln(2)
            this.numHashFunctions = Math.max(1, (int) Math.round(
                    ((double) this.bitArraySize / expectedElements) * Math.log(2)));
        }

        public int getExpectedElements() { return expectedElements; }
        public double getFalsePositiveRate() { return falsePositiveRate; }
        public int getBitArraySize() { return bitArraySize; }
        public int getNumHashFunctions() { return numHashFunctions; }

        @Override
        public String toString() {
            return "BloomFilterConfig{" +
                    "expectedElements=" + expectedElements +
                    ", falsePositiveRate=" + falsePositiveRate +
                    ", bitArraySize=" + bitArraySize +
                    ", numHashFunctions=" + numHashFunctions +
                    '}';
        }
    }

    public interface IBloomFilter {
        void add(String element);
        boolean mightContain(String element);
        void clear();
    }
    public interface IRemovableBloomFilter extends IBloomFilter {
        void remove(String element);
    }

    public class BloomFilter implements IBloomFilter {
        private final BloomFilterConfig config;
        private final AtomicBitArray bitArray;
        private final HashFunction hashFunction;

        public BloomFilter(BloomFilterConfig config, HashFunction hashFunction) {
            Objects.requireNonNull(config);
            Objects.requireNonNull(hashFunction);
            this.config = config;
            this.bitArray = new AtomicBitArray(config.getBitArraySize()); // BitSet(size) or BitArray(boolean[size])
            this.hashFunction = hashFunction;
        }

        public void add(String element) {
            Objects.requireNonNull(element);

            int bitArraySize = config.getBitArraySize();
            int numHashFunctions = config.getNumHashFunctions();

            for (int i = 0; i < numHashFunctions; ++i) {
                int position = hashFunction.hash(element, i, bitArraySize);
                bitArray.set(position);
            }
        }

        public boolean mightContain(String element) {
            Objects.requireNonNull(element);

            int bitArraySize = config.getBitArraySize();
            int numHashFunctions = config.getNumHashFunctions();

            for (int i = 0; i < numHashFunctions; ++i) {
                int position = hashFunction.hash(element, i, bitArraySize);
                if (!bitArray.get(position))
                    return false;
            }
            return true;
        }

        public void clear() {
            bitArray.clear();
        }
    }

    public class CountingBloomFilter implements IRemovableBloomFilter {
        private final BloomFilterConfig config;
        private final int[] counters;
        private final HashFunction hashFunction;

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        public CountingBloomFilter(BloomFilterConfig config, HashFunction hashFunction) {
            Objects.requireNonNull(config);
            Objects.requireNonNull(hashFunction);
            this.config = config;
            this.counters = new int[config.getBitArraySize()];
            this.hashFunction = hashFunction;
        }

        public void add(String element) {
            Objects.requireNonNull(element);

            int bitArraySize = config.getBitArraySize();
            int numHashFunctions = config.getNumHashFunctions();

            lock.writeLock().lock();
            try {
                for (int i = 0; i < numHashFunctions; ++i) {
                    int pos = hashFunction.hash(element, i, bitArraySize);
                    counters[pos]++;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        public void remove(String element) {
            Objects.requireNonNull(element);

            int bitArraySize = config.getBitArraySize();
            int numHashFunctions = config.getNumHashFunctions();

            lock.writeLock().lock();
            try {
                for (int i = 0; i < numHashFunctions; ++i) {
                    int pos = hashFunction.hash(element, i, bitArraySize);
                    if (counters[pos] > 0)
                        --counters[pos];
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean mightContain(String element) {
            Objects.requireNonNull(element);

            int bitArraySize = config.getBitArraySize();
            int numHashFunctions = config.getNumHashFunctions();

            lock.readLock().lock();
            try {
                for (int i = 0; i < numHashFunctions; ++i) {
                    int pos = hashFunction.hash(element, i, bitArraySize);
                    if (counters[pos] == 0)
                        return false;
                }
                return true;
            } finally {
                lock.readLock().unlock();
            }
        }

        public void clear() {
            lock.writeLock().lock();
            try {
                Arrays.fill(counters, 0);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public class ScalableBloomFilter implements IBloomFilter {
        private final List<IBloomFilter> filters = new ArrayList<>();
        private final HashFunction hashFunction;
        private final int initialCapacity;
        private final double initialFpRate;

        private IBloomFilter currentFilter;
        private int capacityPerFilter;
        private int countInCurrent;
        private double currentFpRate;

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        public ScalableBloomFilter(int initialCapacity, double initialFpRate, HashFunction hashFunction) {
            if (initialCapacity <= 0 || initialFpRate <= 0 || initialFpRate >= 1)
                throw new IllegalArgumentException("initialCapacity must be > 0 and initialFpRate must be between 0 and 1");
            this.initialCapacity = initialCapacity;
            this.initialFpRate = initialFpRate;
            this.capacityPerFilter = initialCapacity;
            this.currentFpRate = initialFpRate;
            this.hashFunction = hashFunction;
            addFilter();
        }

        private void addFilter() {
            filters.add(new BloomFilter(new BloomFilterConfig(capacityPerFilter, currentFpRate), hashFunction));
            currentFilter = filters.get(filters.size() - 1);
            countInCurrent = 0;
        }

        public void add(String element) {
            Objects.requireNonNull(element);
            lock.writeLock().lock();
            try {
                if (countInCurrent >= capacityPerFilter) {
                    capacityPerFilter *= 2;       // grow capacity
                    currentFpRate *= 0.5;         // tighten rate so total stays bounded
                    addFilter();
                }
                currentFilter.add(element);
                ++countInCurrent;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean mightContain(String element) {
            lock.readLock().lock();
            try {
                for (IBloomFilter filter : filters)
                    if (filter.mightContain(element))
                        return true;
                return false;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void clear() {
            lock.writeLock().lock();
            try {
                filters.clear();

                countInCurrent = 0;
                capacityPerFilter = initialCapacity;
                currentFpRate = initialFpRate;

                addFilter();
            } finally {
                lock.writeLock().unlock();
            }
        }

            public int numberOfFilters() {
                lock.readLock().lock();
                try {
                    return filters.size();
                } finally {
                    lock.readLock().unlock();
                }
            }
    }
}
