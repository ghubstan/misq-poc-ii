package org.misq.wallet.installer;

import org.misq.common.di.module.AbstractDependencyInjectionModule;

public class WalletInstallerDependencyInjectionModule extends AbstractDependencyInjectionModule {

    public WalletInstallerDependencyInjectionModule() {
    }

    @Override
    public void configure() {
        createMapping(WalletInstallerApi.class, new WalletInstallerApi());
    }
}
