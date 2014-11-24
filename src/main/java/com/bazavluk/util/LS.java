package com.bazavluk.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Simple DI
 */
public class LS {
    private static Map<Class, Object> objects = new LinkedHashMap<>();

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
