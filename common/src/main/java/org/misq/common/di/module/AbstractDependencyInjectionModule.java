package org.misq.common.di.module;


import org.misq.common.di.annotation.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Abstract module to configure and retrieve proper dependency injection mapping.
 * Modified from https://github.com/zeldan/your-own-dependency-injection-framework.
 */
public abstract class AbstractDependencyInjectionModule implements DependencyInjectionModule {

    private final Map<Class<?>, Object> singletonMap = new HashMap<>();
    private final Map<String, Object> objectMap = new HashMap<>();
    private final Map<Class<?>, Class<?>> classMap = new HashMap<>();

    public final Predicate<Class<?>> isSingleton = (type) -> type.isAnnotationPresent(Singleton.class);

    @Override
    public Object getMapping(final String key) {
        final Object value = objectMap.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Couldn't find value mapping for key " + key);
        }
        return value;
    }

    @Override
    public <T> Class<? extends T> getMapping(final Class<T> type) {
        final Class<?> implementation = classMap.get(type);
        if (implementation == null) {
            throw new IllegalArgumentException("Couldn't find the mapping (subclass / implementation) for " + type);
        }
        return implementation.asSubclass(type);
    }

    @Override
    public Object getSingletonMapping(final Class<?> type) {
        final Object singleton = singletonMap.get(type);
        if (singleton == null) {
            throw new IllegalArgumentException("Couldn't find the mapped singleton object for " + type);
        }
        return singleton;
    }

    protected void createMapping(final String key, final Object value) {
        if (!objectMap.containsKey(key)) {
            objectMap.put(key, value);
        } else {
            throw new IllegalStateException("Attempt to cache key/value already present in object map.");
        }
    }

    protected void createMapping(Class<?> type, final Object value) {
        if (isSingleton.test(type)) {
            createSingletonMapping(type, value);
        } else {
            String className = type.getName();
            if (!objectMap.containsKey(className)) {
                objectMap.put(className, value);
            } else {
                throw new IllegalStateException("Attempt to cache object already present in object map.");
            }
        }
    }

    protected <T> void createMapping(final Class<T> baseClass, final Class<? extends T> subClass) {
        classMap.put(baseClass, subClass.asSubclass(baseClass));
    }

    private <T> void createSingletonMapping(final Class<T> singletonClass, final Object value) {
        if (!isSingleton.test(singletonClass)) {
            throw new IllegalStateException("Attempt to cache singleton object that has no @Singleton annotation on class declaration.");
        }
        if (!singletonMap.containsKey(singletonClass)) {
            singletonMap.put(singletonClass, value);
        } else {
            throw new IllegalStateException("Attempt to cache singleton object already present in singleton object map.");
        }
    }
}