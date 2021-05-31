package org.misq.common.di.module;

/**
 * SimpleModule interface to configure dependency injection mapping.
 * Modified from https://github.com/zeldan/your-own-dependency-injection-framework.
 */
public interface DependencyInjectionModule {

    void configure();

    Object getSingletonMapping(Class<?> type);

    Object getMapping(final String key);

    <T> Class<? extends T> getMapping(Class<T> type);
}