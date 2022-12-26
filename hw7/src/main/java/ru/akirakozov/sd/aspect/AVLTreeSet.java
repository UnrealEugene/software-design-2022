package ru.akirakozov.sd.aspect;

import java.util.*;

@Profile(showTree = true)
public class AVLTreeSet<T> extends AbstractSet<T> implements SortedSet<T> {
    private int size;

    private Node<T> root;

    private final Comparator<? super T> comparator;

    public AVLTreeSet() {
        this(List.of(), null);
    }

    public AVLTreeSet(Comparator<? super T> comparator) {
        this(List.of(), comparator);
    }

    public AVLTreeSet(Collection<T> data) {
        this(data, null);
    }

    public AVLTreeSet(Collection<T> data, Comparator<? super T> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;

        this.addAll(data);
    }

    @SuppressWarnings("unchecked")
    private int compare(T left, T right) {
        return comparator == null ?
                ((Comparable<? super T>) left).compareTo(right) : comparator.compare(left, right);
    }

    @Override
    public Iterator<T> iterator() {
        return new AVLTreeSetIterator();
    }

    @Override
    public boolean add(T value) {
        if (contains(value)) {
            return false;
        }
        root = add(root, value);
        size++;
        return true;
    }

    private Node<T> add(Node<T> cur, T value) {
        if (cur == null) {
            return new Node<>(value);
        }
        if (compare(value, cur.value) < 0) {
            connectLeft(cur, add(cur.left, value));
        } else {
            connectRight(cur, add(cur.right, value));
        }
        return balance(cur);
    }

    private Node<T> removeMin(Node<T> cur) {
        assert cur != null;
        if (cur.left == null) {
            return cur.right;
        }
        connectLeft(cur, removeMin(cur.left));
        return balance(cur);
    }

    private void connectLeft(Node<T> parent, Node<T> child) {
        if (parent != null) {
            parent.left = child;
        }
        if (child != null) {
            child.parent = parent;
        }
    }

    private void connectRight(Node<T> parent, Node<T> child) {
        if (parent != null) {
            parent.right = child;
        }
        if (child != null) {
            child.parent = parent;
        }
    }

    private Node<T> findMin(Node<T> cur) {
        if (cur == null) {
            return null;
        }
        if (cur.left == null) {
            return cur;
        }
        return findMin(cur.left);
    }

    private Node<T> findMax(Node<T> cur) {
        if (cur == null) {
            return null;
        }
        if (cur.right == null) {
            return cur;
        }
        return findMax(cur.right);
    }

    private Node<T> successor(Node<T> cur) {
        if (cur == null) {
            return null;
        }
        if (cur.right != null) {
            return findMin(cur.right);
        }
        while (cur.parent != null) {
            if (cur == cur.parent.left) {
                return cur.parent;
            }
            cur = cur.parent;
        }
        return null;
    }

    private Node<T> remove(Node<T> cur, T value) {
        if (cur == null) {
            return null;
        }
        if (compare(cur.value, value) == 0) {
            if (cur.right == null) {
                return cur.left;
            }
            Node<T> t = findMin(cur.right);
            Node<T> q = removeMin(cur.right);
            connectLeft(t, cur.left);
            connectRight(t, q);
            return balance(t);
        }
        if (compare(value, cur.value) < 0) {
            connectLeft(cur, remove(cur.left, value));
        }
        if (compare(value, cur.value) > 0) {
            connectRight(cur, remove(cur.right, value));
        }
        return balance(cur);
    }

    private Node<T> balance(Node<T> node) {
        Node<T> parent = node.parent;
        recalculate(node);
        if (diff(node) == -2) {
            if (diff(node.right) > 0) {
                node.right = rotateRight(node.right);
                connectRight(node, node.right);
            }
            Node<T> newNode = rotateLeft(node);
            newNode.parent = parent;
            return newNode;
        }
        if (diff(node) == 2) {
            if (diff(node.left) < 0) {
                node.left = rotateLeft(node.left);
                connectLeft(node, node.left);
            }
            Node<T> newNode = rotateRight(node);
            newNode.parent = parent;
            return newNode;
        }
        return node;
    }

    private Node<T> rotateLeft(Node<T> a) {
        if (a == null || a.right == null) {
            return a;
        }
        Node<T> b = a.right;
        connectRight(a, b.left);
        connectLeft(b, a);
        recalculate(a);
        recalculate(b);
        return b;
    }

    private Node<T> rotateRight(Node<T> a) {
        if (a == null || a.left == null) {
            return a;
        }
        Node<T> b = a.left;
        connectLeft(a, b.right);
        connectRight(b, a);
        recalculate(a);
        recalculate(b);
        return b;
    }

    private int diff(Node<T> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = node.left != null ? node.left.height : 0;
        int rightHeight = node.right != null ? node.right.height : 0;
        return leftHeight - rightHeight;
    }

    private void recalculate(Node<T> node) {
        if (node == null) {
            return;
        }
        int leftHeight = node.left != null ? node.left.height : 0;
        int rightHeight = node.right != null ? node.right.height : 0;
        node.height = Math.max(leftHeight, rightHeight) + 1;
    }

    private Node<T> find(Node<T> cur, T value) {
        if (cur == null) {
            return null;
        }
        if (cur.value == value) {
            return cur;
        }
        if (compare(value, cur.value) < 0) {
            return find(cur.left, value);
        } else {
            return find(cur.right, value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object value) {
        if (!contains(value)) {
            return false;
        }
        root = remove(root, (T) value);
        size--;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object value) {
        return find(root, (T) value) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T first() {
        return findMin(root).value;
    }

    @Override
    public T last() {
        return findMax(root).value;
    }

    private static class Node<T> {
        Node<T> left;
        Node<T> right;
        Node<T> parent;
        int height;
        T value;

        Node(T value) {
            this.height = 1;
            this.value = value;
        }
    }

    private class AVLTreeSetIterator implements Iterator<T> {
        Node<T> node;

        public AVLTreeSetIterator() {
            this.node = findMin(root);
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            if (node == null) {
                throw new NoSuchElementException();
            }
            T value = node.value;
            node = successor(node);
            return value;
        }
    }
}
