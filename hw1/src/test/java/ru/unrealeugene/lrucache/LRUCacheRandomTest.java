package ru.unrealeugene.lrucache;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class LRUCacheRandomTest {
    private static final long RANDOM_SEED = 45;
    private static final int MIN_CAPACITY = 2;
    private static final int MAX_CAPACITY = 8;
    private static final int MAX_KEY = 2 * MAX_CAPACITY;
    private static final int ITERATIONS = 100000;

    @Test
    public void randomTest() {
        Random random = new Random(RANDOM_SEED);

        for (int capacity = MIN_CAPACITY; capacity <= MAX_CAPACITY; capacity++) {
            LRUCache<Integer, Integer> lruCache = new LRUCache<>(capacity);
            Map<Integer, Integer> lruCacheReal = new LRUCacheReal<>(capacity);

            System.out.printf("Testing LRUCache with capacity %d...%n", capacity);

            for (int iter = 0; iter < ITERATIONS; iter++) {
                int key = random.nextInt(MAX_KEY);
                if (random.nextBoolean()) {
                    if (random.nextBoolean()) {
                        int value = random.nextInt(MAX_KEY);
                        Assert.assertEquals(lruCacheReal.put(key, value), lruCache.put(key, value));
                    } else {
                        Assert.assertEquals(lruCacheReal.remove(key), lruCache.remove(key));
                    }
                } else {
                    Assert.assertEquals(lruCacheReal.get(key), lruCache.get(key));
                }
                Assert.assertEquals(lruCache.size(), lruCacheReal.size());
            }
        }
    }

    private static class LRUCacheReal<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRUCacheReal(int capacity) {
            super(capacity, .75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }
}
