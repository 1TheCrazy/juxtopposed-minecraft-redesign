package dev.onethecrazy.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class RecentList<T> {
    private final int capacity;
    private final Deque<T> deque = new ArrayDeque<>();

    public RecentList(int capacity) {
        this.capacity = capacity;
    }

    public void add(T value) {
        if (deque.size() == capacity) {
            deque.removeFirst(); // ONLY removal point
        }
        deque.addLast(value);
    }

    // We make sure oldest and newest is not the same element, which only works for size >= 2 (which is conveniently what we use)
    public T getOldest() {
        return deque.size() >= 2 ? deque.peekFirst() : null;
    }

    public T getNewest() {
        return deque.peekLast();
    }

    public boolean contains(T value) {
        return deque.contains(value);
    }

    public List<T> asList() {
        return List.copyOf(deque); // immutable snapshot
    }

    public int size() {
        return deque.size();
    }
}
