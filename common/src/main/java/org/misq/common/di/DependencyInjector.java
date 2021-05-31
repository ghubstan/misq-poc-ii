package org.misq.common.di;


import org.misq.common.di.annotation.Inject;
import org.misq.common.di.annotation.Singleton;
import org.misq.common.di.module.DependencyInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.lang.System.identityHashCode;


/**
 * Dependency Injection framework suitable for Constructor and Field injection.
 * Modified from https://github.com/zeldan/your-own-dependency-injection-framework.
 */
public class DependencyInjector {
    private static final Logger log = LoggerFactory.getLogger(DependencyInjector.class);

    private final DependencyInjectionModule module;

    private final ConcurrentHashMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public final Predicate<Class<?>> isSingleton = (type) -> type.isAnnotationPresent(Singleton.class);

    public DependencyInjector(DependencyInjectionModule module) {
        this.module = module;
    }

    public DependencyInjectionModule getModule() {
        return module;
    }

    public Object getInstance(Class<?> injectedClass) {
        if (instances.containsKey(injectedClass)) {
            return instances.get(injectedClass);
        } else if (instances.get(injectedClass) == null) {
            throw new IllegalStateException(format("Instance of injected type %s is null.", injectedClass.getName()));
        } else {
            throw new IllegalStateException(format("No instance of injected type %s found.", injectedClass.getName()));
        }
    }

    public Object inject(final Class<?> classToInject) {
        try {
            if (classToInject == null) {
                return null;
            }

            if (instances.containsKey(classToInject)) {
                Object instance = instances.get(classToInject);
                log.warn("Injector has already injected an {} instance, returning instance with identityHashCode {}",
                        classToInject.getSimpleName(),
                        identityHashCode(instance));
                return instance;
            }

            Object instance = injectFieldsIntoClass(classToInject);
            instances.put(classToInject, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }

    private Object injectFieldsIntoClass(final Class<?> classToInject)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (final Constructor<?> constructor : classToInject.getConstructors()) {

            if (constructor.isAnnotationPresent(Inject.class)) {
                return injectFieldsViaConstructor(classToInject, constructor);
            } else {
                return injectFields(classToInject);
            }
        }
        return null;
    }

    private Object injectFields(Class<?> classToInject)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object o = classToInject.getDeclaredConstructor().newInstance();
        for (Field field : classToInject.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                final Object dependency;
                switch (field.getType().getName()) {
                    case "int", "java.lang.Integer", "java.lang.BigInteger", "java.lang.AtomicInteger", "double",
                            "java.lang.Double", "java.lang.BigDecimal", "java.lang.Byte", "float", "java.lang.Float",
                            "long", "java.lang.Long", "java.lang.AtomicLong", "java.lang.String" -> {
                        dependency = module.getMapping(field.getName());
                        field.set(o, dependency);
                    }
                    default -> {
                        Class<?> fieldType = field.getType();
                        if (isSingleton.test(fieldType)) {
                            dependency = module.getSingletonMapping(fieldType);
                            field.set(o, dependency);
                        } else {
                            dependency = module.getMapping(fieldType);
                            field.set(o, ((Class<?>) dependency).getConstructor().newInstance());
                        }
                    }
                }
            }
        }
        return o;
    }

    private Object injectFieldsViaConstructor(Class<?> classToInject, Constructor<?> constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] objArr = new Object[parameterTypes.length];
        int i = 0;
        for (final Class<?> c : parameterTypes) {
            final Object dependency = isSingleton.test(c)
                    ? module.getSingletonMapping(c)
                    : module.getMapping(c.getName());
            if (c.isAssignableFrom(dependency.getClass())) {
                objArr[i++] = dependency;
            }
        }
        return classToInject.getConstructor(parameterTypes).newInstance(objArr);
    }
}
