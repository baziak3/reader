package com.bazavluk.util;

/**
 * Created by Папа on 29.09.14.
 */
public class Mutable<T> {
    T v;
    public Mutable(T v) {
        this.v = v;
    }
    public void set(T v) {
        this.v = v;
    }
    public T get() {
        return v;
    }
}
