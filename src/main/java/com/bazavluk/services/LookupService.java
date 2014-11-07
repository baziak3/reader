package com.bazavluk.services;

import java.util.HashMap;
import java.util.Map;

public class LookupService {
    private static Map<Class, Object> objects = new HashMap<>();

    public static void register(Object o) {
        register(o, o.getClass());
    }

    public static void register(Object o, Class clazz) {
        objects.put(clazz, o);
    }

    @SuppressWarnings("unchecked")
    public static <T>T get(Class<T> clazz) {
        return (T) objects.get(clazz);
    }
}
