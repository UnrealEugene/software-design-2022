package ru.unrealeugene.lrucache;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LRUCacheSimpleTest {
    @Test
    public void leetCodeTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(2);
        List<Integer> expected = new ArrayList<>(Arrays.asList(null, null, 1, null, null, null, null, 3, 4));
        List<Integer> actual = new ArrayList<>();

        actual.add(lruCache.put(1, 1));
        actual.add(lruCache.put(2, 2));
        actual.add(lruCache.get(1));
        actual.add(lruCache.put(3, 3));
        actual.add(lruCache.get(2));
        actual.add(lruCache.put(4, 4));
        actual.add(lruCache.get(1));
        actual.add(lruCache.get(3));
        actual.add(lruCache.get(4));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void capacityOneTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(1);

        lruCache.put(1, 1);
        Assert.assertEquals(1, (int) lruCache.get(1));

        lruCache.put(2, 2);
        Assert.assertEquals(2, (int) lruCache.get(2));

        Assert.assertNull(lruCache.get(1));

        Assert.assertEquals(2, (int) lruCache.remove(2));
        Assert.assertNull(lruCache.remove(2));
        Assert.assertEquals(0, lruCache.size());
    }
}
