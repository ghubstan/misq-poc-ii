package org.misq.common.di;

import org.misq.common.di.module.DependencyInjectionModule;

/**
 * The entry point to the Misq IOC framework. Creates DependencyInjector from a SimpleModule.
 * <p>
 * Does not support multi-module injectors.
 */
public final class Zuice {

    public static DependencyInjector createInjector(DependencyInjectionModule module) {
        return DependencyInjectorConfig.getInjector(module);
    }
}
