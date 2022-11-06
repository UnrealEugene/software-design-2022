package ru.unrealeugene.lrucache;

import java.util.*;

public class LRUCache<K, V> {
    private final long capacity;
    private final HashMap<K, Node<K, V>> nodeMap;
    private Node<K, V> head;
    private Node<K, V> tail;

    public LRUCache(long capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer");
        }
        this.capacity = capacity;
        this.head = null;
        this.tail = null;
        this.nodeMap = new HashMap<>();
    }

    public V get(K key) {
        assert nodeMap.size() <= capacity;

        if (!nodeMap.containsKey(key)) {
            return null;
        }

        Node<K, V> node = nodeMap.get(key);
        disconnectNode(node);
        appendNode(node);

        assert nodeMap.size() <= capacity && nodeMap.containsKey(key)
                && nodeMap.get(key) == head && head.key == key
                && head.next == null && tail.prev == null;
        return node.value;
    }

    public V put(K key, V value) {
        assert nodeMap.size() <= capacity;

        V prevValue = null;
        if (nodeMap.containsKey(key)) {
            prevValue = remove(key);
        }

        Node<K, V> node = new Node<>(key, value);
        appendNode(node);
        nodeMap.put(key, node);

        if (nodeMap.size() > capacity) {
            remove(tail.key);
        }

        assert nodeMap.size() <= capacity && nodeMap.containsKey(key)
                && nodeMap.get(key) == head && head.key == key && head.value == value
                && head.next == null && tail.prev == null;
        return prevValue;
    }

    public V remove(K key) {
        if (!nodeMap.containsKey(key)) {
            return null;
        }

        Node<K, V> node = nodeMap.get(key);
        disconnectNode(node);
        nodeMap.remove(key);

        assert nodeMap.size() <= capacity && !nodeMap.containsKey(key)
                && (head == null || head.next == null)
                && (tail == null || tail.prev == null);
        return node.value;
    }

    public int size() {
        return nodeMap.size();
    }

    private void appendNode(Node<K, V> node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.prev = head;
            head.next = node;
            head = node;
        }
    }

    private void disconnectNode(Node<K, V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        if (node == tail) {
            tail = tail.next;
        }
        if (node == head) {
            head = head.prev;
        }
        node.next = null;
        node.prev = null;
    }

    private static class Node<K, V> {
        private final K key;
        private final V value;
        private Node<K, V> prev;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
