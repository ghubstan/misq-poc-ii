package org.misq.application;

import org.misq.api.CoreApi;
import org.misq.common.di.module.AbstractDependencyInjectionModule;
import org.misq.wallet.installer.WalletInstallerApi;

public class ApplicationDependencyInjectionModule extends AbstractDependencyInjectionModule {

    private final CoreApi coreApi;
    private final WalletInstallerApi walletInstallerApi;

    public ApplicationDependencyInjectionModule(CoreApi coreApi, WalletInstallerApi walletInstallerApi) {
        this.coreApi = coreApi;
        this.walletInstallerApi = walletInstallerApi;
    }

    @Override
    public void configure() {
        createMapping(CoreApi.class, coreApi);
        createMapping(WalletInstallerApi.class, walletInstallerApi);
    }
}
