package com.cerbon.talk_balloons.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class represents a historical data structure that maintains a history of generic type elements.
 * The history size is limited by a maximum value, and older elements are removed when this limit is exceeded.
 *
 * @param <T> the type of elements in this list
 */
public class HistoricalData<T> implements List<T> {
    private final int maxHistory;
    private final List<T> history;

    public HistoricalData(T initialValue, int maxHistory) {
        if (maxHistory < 2) throw new IllegalArgumentException("Max History cannot be less than 2");
        this.maxHistory = maxHistory;
        this.history = new ArrayList<>();
        this.history.add(initialValue);
    }

    @Override
    public int size() {
        return history.size();
    }

    @Override
    public boolean isEmpty() {
        return history.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return history.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return history.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return history.toArray();
    }

    @NotNull
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return history.toArray(a);
    }

    /**
     * Adds the specified element to the end of the history.
     * If the size of the history exceeds the maximum limit after the addition,
     * the oldest element (at index 0) is removed.
     *
     * @param value the element to be added
     * @return true (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(T value) {
        history.add(value);
        if (history.size() > maxHistory)
            history.remove(0);
        return true;
    }

    @Override
    public void add(int index, T value) {
        throw new UnsupportedOperationException("Add with index is not supported.");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        for (T value : c)
            add(value);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException("AddAll with index is not supported.");
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(history).containsAll(c);
    }

    @Override
    public boolean remove(Object o) {
        return history.remove(o);
    }

    @Override
    public T remove(int index) {
        return history.remove(index);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return history.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return history.retainAll(c);
    }

    @Override
    public void clear() {
        history.clear();
    }

    /**
     * Returns the element at the specified position in the history.
     * The position is calculated from the end of the list, with 0 being the last element,
     * 1 being the second last element, and so on.
     * If the specified position is greater than or equal to the size of the history,
     * the first element of the history is returned.
     *
     * @param past the position in the history to fetch the element from
     * @return the element at the specified position
     * @throws IllegalArgumentException if the specified position is negative
     */
    @Override
    public T get(int past) {
        if (past < 0) throw new IllegalArgumentException("Past cannot be negative");
        int clampedPast = Math.max(history.size() - 1 - past, 0);
        return history.get(clampedPast);
    }

    public List<T> getAll() {
        return new ArrayList<>(history);
    }

    @Override
    public T set(int index, T element) {
        return history.set(index, element);
    }

    @Override
    public int indexOf(Object o) {
        return history.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return history.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return history.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return history.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return history.subList(fromIndex, toIndex);
    }
}