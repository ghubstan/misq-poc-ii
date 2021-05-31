package org.misq.common.di;

import org.misq.common.di.module.DependencyInjectionModule;

/**
 * Initializes dependency injection configuration based on the configuration module; it binds interfaces
 * to implementations.
 * Modified from https://github.com/zeldan/your-own-dependency-injection-framework.
 */
public class DependencyInjectorConfig {

    public static DependencyInjector getInjector(final DependencyInjectionModule module) {
        module.configure();
        return new DependencyInjector(module);
    }
}
