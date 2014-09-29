package com.bazavluk.services;

import java.util.HashMap;
import java.util.Map;

public class LookupService {
    private static Map<Class, Object> objects = new HashMap<>();

    public static void register(Object o) {
        objects.put(o.getClass(), o);
    }

    @SuppressWarnings("unchecked")
    public static <T>T get(Class<T> clazz) {
        return (T) objects.get(clazz);
    }
}
