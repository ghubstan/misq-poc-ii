package org.misq.web.server;

import org.misq.api.CoreApi;
import org.misq.wallet.installer.WalletInstallerApi;
import org.misq.web.json.JsonTransform;
import org.misq.web.server.handler.GetBalanceHandler;
import org.misq.web.server.handler.GetPeersHandler;
import org.misq.web.server.handler.GetVersionHandler;
import org.misq.web.server.handler.InstallWalletHandler;
import ratpack.registry.RegistrySpec;
import ratpack.registry.internal.DefaultRegistryBuilder;

/**
 * The registry makes misq apis and web handlers available through the ratpack context.
 */
class MisqRegistrySpec extends DefaultRegistryBuilder implements RegistrySpec {

    private final JsonTransform jsonTransform;

    MisqRegistrySpec(CoreApi coreApi, WalletInstallerApi walletInstallerApi) {
        this.jsonTransform = new JsonTransform();
        init(coreApi, walletInstallerApi);
    }

    void init(CoreApi coreApi, WalletInstallerApi walletInstallerApi) {
        add(CoreApi.class, coreApi);
        add(WalletInstallerApi.class, walletInstallerApi);

        add(GetBalanceHandler.class, new GetBalanceHandler(jsonTransform));
        add(GetPeersHandler.class, new GetPeersHandler(jsonTransform));
        add(GetVersionHandler.class, new GetVersionHandler(jsonTransform));
        add(InstallWalletHandler.class, new InstallWalletHandler(jsonTransform));
    }
}
