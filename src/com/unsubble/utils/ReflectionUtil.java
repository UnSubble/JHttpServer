package com.unsubble.utils;

import com.unsubble.core.RequestHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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

    public static boolean suits(Class<?> parent, Class<?> toFit) {
        if (parent == null || !parent.equals(toFit.getSuperclass()))
            return false;
        for (Constructor<?> constructor : toFit.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0)
                return true;
        }
        return false;
    }

    public static boolean isAnnotationPresent(Class<?> target, Class<? extends Annotation> annotation) {
        return target.isAnnotationPresent(annotation);
    }
}
