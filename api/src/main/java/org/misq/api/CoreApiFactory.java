package org.misq.api;

import org.misq.common.di.DependencyInjector;
import org.misq.common.di.Zuice;

public class CoreApiFactory {
    public static CoreApi createCoreApi() {
        DependencyInjector injector = Zuice.createInjector(new ApiDependencyInjectionModule());
        injector.inject(CoreApi.class);
        return (CoreApi) injector.getInstance(CoreApi.class);
    }
}
