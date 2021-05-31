package org.misq.wallet.installer;

import org.misq.common.di.DependencyInjector;
import org.misq.common.di.Zuice;

public class WalletInstallerApiFactory {
    public static WalletInstallerApi createWalletInstallerApi() {
        DependencyInjector injector = Zuice.createInjector(new WalletInstallerDependencyInjectionModule());
        injector.inject(WalletInstallerApi.class);
        return (WalletInstallerApi) injector.getInstance(WalletInstallerApi.class);
    }
}
