package com.unsubble.utils;

import java.lang.reflect.InvocationTargetException;

public final class ReflectionUtil {

    private ReflectionUtil() {
        throw new AssertionError();
    }

    public static Object newInstanceWithEmptyConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
