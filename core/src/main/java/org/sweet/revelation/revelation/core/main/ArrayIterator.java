package org.sweet.revelation.revelation.core.main;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;

    private int index;

    public ArrayIterator(E[] array) {
        this.array = array;
    }

    public boolean hasNext() {
        return array != null && index < array.length;
    }

    public E next() {
        E result = array[index];

        ++index;

        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
