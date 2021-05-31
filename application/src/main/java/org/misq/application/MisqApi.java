package org.misq.application;

import org.misq.api.CoreApi;
import org.misq.common.di.annotation.Inject;
import org.misq.common.di.annotation.Singleton;
import org.misq.wallet.installer.WalletInstallerApi;

@Singleton
public class MisqApi {

    private final CoreApi coreApi;
    private final WalletInstallerApi walletInstallerApi;

    @Inject
    public MisqApi(CoreApi coreApi, WalletInstallerApi walletInstallerApi) {
        this.coreApi = coreApi;
        this.walletInstallerApi = walletInstallerApi;
    }

    public CoreApi getCoreApi() {
        return coreApi;
    }

    public WalletInstallerApi getWalletInstallerApi() {
        return walletInstallerApi;
    }
}
