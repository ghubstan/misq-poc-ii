package org.misq.api;

import org.misq.common.di.module.AbstractDependencyInjectionModule;
import org.misq.p2p.P2PService;

public class ApiDependencyInjectionModule extends AbstractDependencyInjectionModule {

    @Override
    public void configure() {
        createMapping(P2PService.class, new P2PService());
    }
}
